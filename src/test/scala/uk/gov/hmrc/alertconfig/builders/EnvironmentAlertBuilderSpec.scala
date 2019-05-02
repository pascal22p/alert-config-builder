package uk.gov.hmrc.alertconfig.builders

import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import spray.json._

class EnvironmentAlertBuilderSpec  extends WordSpec with Matchers with BeforeAndAfterEach {

  "EnvironmentAlertBuilder" should {
    "create config with production enabled with default severities" in {
      EnvironmentAlertBuilder("team-telemetry").inProduction().alertConfigFor(Production) shouldBe
        JsObject("team-telemetry" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team iprights -e aws_production"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
            "filter" -> JsString("occurrences")))
    }

    "create config with integration enabled with default severities" in {
      EnvironmentAlertBuilder("team-telemetry").inIntegration().alertConfigFor(Integration) shouldBe
        JsObject("team-telemetry" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team iprights -e aws_integration"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
            "filter" -> JsString("occurrences")))
    }

    "create config with integration disabled" in {
      EnvironmentAlertBuilder("team-telemetry").alertConfigFor(Integration) shouldBe
        JsObject("team-telemetry" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/noop.rb"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
            "filter" -> JsString("occurrences")))
    }

    "create config with integration enabled with custom severities" in {
      EnvironmentAlertBuilder("team-telemetry").inIntegration(Set(Ok, Warning, Critical, Unknown)).alertConfigFor(Integration) shouldBe
        JsObject("team-telemetry" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team iprights -e aws_integration"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical"), JsString("unknown")),
            "filter" -> JsString("occurrences")))
    }


  }
}
