package uk.gov.hmrc.alertconfig.builders

import org.scalatest.{BeforeAndAfterEach, FunSuite, Matchers}
import spray.json.{JsArray, JsObject, JsString}

class AllEnvironmentAlertConfigBuilderSpec extends FunSuite with Matchers with BeforeAndAfterEach {

  def defaultNoopHandlerConfig: JsObject =
    JsObject(
      "command" -> JsString("/etc/sensu/handlers/noop.rb"),
      "type" -> JsString("pipe"),
      "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
      "filter" -> JsString("occurrences"))

  def defaultEnabledHandlerConfig(service: String, environment: Environment): JsObject =
    JsObject(
      "command" -> JsString(s"/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team $service -e $environment"),
      "type" -> JsString("pipe"),
      "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
      "filter" -> JsString("occurrences"))

    Seq(Integration, Development, Staging, Qa, ExternalTest, Management).foreach{
    e =>
      test(s"create config for $e") {
        val environmentConfigMap = AllEnvironmentAlertConfigBuilder.build(
          Set(EnvironmentAlertBuilder("team-telemetry"), EnvironmentAlertBuilder("infra")))

        environmentConfigMap(e) shouldBe
          JsObject("handlers" -> JsObject(
            "infra" -> defaultNoopHandlerConfig,
            "team-telemetry" -> defaultNoopHandlerConfig
          )
          )
      }
    }

    test("create config for production") {
      val environmentConfigMap = AllEnvironmentAlertConfigBuilder.build(
        Set(EnvironmentAlertBuilder("team-telemetry"), EnvironmentAlertBuilder("infra")))

      environmentConfigMap(Production) shouldBe
        JsObject("handlers" -> JsObject(
          "infra" -> defaultEnabledHandlerConfig("infra", Production),
          "team-telemetry" -> defaultEnabledHandlerConfig("team-telemetry", Production)
        )
        )
    }

}
