import awscala.Credentials
import config.AWSCredentials
import org.apache.spark.SparkContext

import java.time.{Clock, LocalDateTime}
import java.time.format.DateTimeFormatter

package object util {

  val pattern = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")

  def timestampString(now: LocalDateTime) = s"${now.format(pattern)}"

  def timestampString(implicit clock: Clock) = s"${LocalDateTime.now(clock).format(pattern)}"

  implicit def toAWSScalaCredentials(defaultAWSCredentials: Credentials): AWSCredentials =
    AWSCredentials(defaultAWSCredentials.getAWSAccessKeyId(), defaultAWSCredentials.getAWSSecretKey())

  implicit class SparkContextOps(val sc: SparkContext) extends AnyVal {
    def setAWSCredentials(defaultAWSCreds: AWSCredentials): Unit = {
      sc.hadoopConfiguration.set("fs.s3a.access.key", defaultAWSCreds.accessKeyId)
      sc.hadoopConfiguration.set("fs.s3a.secret.key", defaultAWSCreds.secretAccessKey)
    }
  }
}
