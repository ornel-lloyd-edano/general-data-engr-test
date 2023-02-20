package config

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object ConfigurationProvider {
  val appConfig: AppConfiguration =
    ConfigSource.default.load[AppConfiguration] match {
      case Left(configReaderFailures) =>
        throw new Exception(s"Cannot read conf file: ${configReaderFailures.toList.map(_.description).mkString(" ")}")
      case Right(config) =>
        config
    }
}
