package application

import application.Parser._
import config.FileSystemConfig
import org.apache.spark.{SparkConf, SparkContext}
import util._

import java.time.Clock

class Application(algorithm: Algorithm)(implicit clock: Clock) {
  private val sparkConf = new SparkConf().setAppName("Odd Number Occurrence")
    .setMaster("local[*]").set("spark.executor.memory","1g");
  private val sc = new SparkContext(sparkConf)

  def run(awsConfig: config.AWSConfig): Unit = {
    lazy val defaultAWSCreds: config.AWSCredentials = awscala.CredentialsLoader.load().getCredentials()
    val customAWSCreds = awsConfig.credentials
    val awsCreds = customAWSCreds.fold(defaultAWSCreds)(identity(_))
    sc.setAWSCredentials(awsCreds)

    val input = awsConfig.s3.inputBucket
    val output = awsConfig.s3.outputBucket.format(timestampString)
    run(input, output)
  }

  def run(fileSystemConfig: FileSystemConfig): Unit = {
    val input = fileSystemConfig.inputFile
    val output = fileSystemConfig.outputFile.format(timestampString)
    run(input, output)
  }

  private def run(input: String, output: String): Unit = {
    val keyValues = sc.textFile(input)
      .collect {
        parseKeyValueFromCSVLine orElse parseKeyValueFromTSVLine
      }

    algorithm.getKeyValuesWithOddOccurrences(keyValues)
      .map(printAndStringifyKeyValuesWithOddOccurrences)
      .coalesce(1)
      .sortBy(identity(_))
      .saveAsTextFile(output)
  }

  private def printAndStringifyKeyValuesWithOddOccurrences: PartialFunction[KeyValueWithOddOccurrences, String] = {
    case (key, Some((valueWhichOccurredOddNumTimes, oddNumTimes))) =>
      println(s"key: $key, value: $valueWhichOccurredOddNumTimes occurred $oddNumTimes times")
      s"$key\t$valueWhichOccurredOddNumTimes"
  }
}
