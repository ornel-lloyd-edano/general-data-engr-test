package object config {
  case class AWSCredentials(accessKeyId: String, secretAccessKey: String)
  case class S3(inputBucket: String, outputBucket: String)
  case class AWSConfig(credentials: Option[AWSCredentials], s3: S3)
  case class FileSystemConfig(inputFile: String, outputFile: String)
  case class AppConfiguration(aws: AWSConfig, fileSystem: FileSystemConfig)
}
