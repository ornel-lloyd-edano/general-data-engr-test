import application.{Application, DefaultAlgorithm}
import config.ConfigurationProvider
import org.slf4j.LoggerFactory

import java.time.Clock

object Main extends App {
  implicit val clock = Clock.systemUTC()
  val logger = LoggerFactory.getLogger("Main")
  try {
    val appConfig = ConfigurationProvider.appConfig
    new Application(DefaultAlgorithm).run(appConfig.aws)

  } catch {
    case _: java.lang.IllegalAccessError =>
      logger.error(s"This version of Spark has issues with Java 17. " +
        s"Please downgrade to Java 11. Go to File> Project Structure> Project Settings> Project> SDK")
    case _: java.nio.file.AccessDeniedException =>
      logger.error("Invalid AWS credentials or credentials was not set")
    case exception: Throwable =>
      exception.printStackTrace()
  }

}