/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.alertconfig.builders

import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import spray.json._

class EnvironmentAlertBuilderSpec  extends WordSpec with Matchers with BeforeAndAfterEach {

  "EnvironmentAlertBuilder" should {
    "create config with production enabled with default severities" in {
      EnvironmentAlertBuilder("team-telemetry").inProduction().alertConfigFor(Production) shouldBe
        "team-telemetry" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team team-telemetry -e aws_production"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
            "filter" -> JsString("occurrences"))
    }

    "create config with integration enabled with default severities" in {
      EnvironmentAlertBuilder("infra").inIntegration().alertConfigFor(Integration) shouldBe
        "infra" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team infra -e aws_integration"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
            "filter" -> JsString("occurrences"))
    }

    "create config with integration disabled" in {
      EnvironmentAlertBuilder("team-telemetry-test").alertConfigFor(Integration) shouldBe
        "team-telemetry-test" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/noop.rb"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical")),
            "filter" -> JsString("occurrences"))
    }

    "create config with integration enabled with custom severities" in {
      EnvironmentAlertBuilder("team-telemetry").inIntegration(Set(Ok, Warning, Critical, Unknown)).alertConfigFor(Integration) shouldBe
        "team-telemetry" ->
          JsObject(
            "command" -> JsString("/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team team-telemetry -e aws_integration"),
            "type" -> JsString("pipe"),
            "severities" ->  JsArray(JsString("ok"), JsString("warning"), JsString("critical"), JsString("unknown")),
            "filter" -> JsString("occurrences"))
    }


  }
}
