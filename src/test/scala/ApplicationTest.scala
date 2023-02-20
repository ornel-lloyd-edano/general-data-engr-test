import application.{Application, DefaultAlgorithm}
import config.ConfigurationProvider
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import util.timestampString

import java.time.{Clock, LocalDateTime, ZoneId, ZoneOffset}
import scala.io.Source

class ApplicationTest extends  AnyWordSpec with Matchers {

  "Application" should {
    "read input files and get the value with odd number occurrences of a given key" in {
      val now =  LocalDateTime.now()
      implicit val clock = Clock.fixed(now.toInstant(ZoneOffset.UTC),  ZoneId.of("GMT"))
      val config = ConfigurationProvider.appConfig
      new Application(DefaultAlgorithm).run(config.fileSystem)
      val output = Source.fromFile(s"src/main/resources/output/${timestampString(now)}/part-00000").getLines()
        .toSeq.map(_.split("\t")).map(item=> (item.head.toInt, item.last.toInt)).toMap

      output.get(1) mustBe Some(500)
      output.get(2) mustBe Some(34)
      output.get(3) mustBe Some(23)
      output.get(4) mustBe Some(99)
      output.get(5) mustBe Some(200)
      output.get(6) mustBe Some(9)
    }
  }

}
