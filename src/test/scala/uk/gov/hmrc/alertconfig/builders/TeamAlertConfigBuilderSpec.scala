/*
 * Copyright 2017 HM Revenue & Customs
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

import org.scalatest.{Matchers, WordSpec}

class TeamAlertConfigBuilderSpec extends WordSpec with Matchers {

  "teamAlerts" should {
    "return TeamAlertConfigBuilder with correct default values" in {

      val alertConfigBuilder = TeamAlertConfigBuilder.teamAlerts(Seq("service1", "service2"))

      alertConfigBuilder.services shouldBe Seq("service1", "service2")
      alertConfigBuilder.handlers shouldBe Seq("noop")
      alertConfigBuilder.http5xxPercentThreshold shouldBe 100
      alertConfigBuilder.http5xxThreshold shouldBe 2
      alertConfigBuilder.exceptionThreshold shouldBe 2
    }

    "throw exception if no service provided" in {

      an [RuntimeException] should be thrownBy TeamAlertConfigBuilder.teamAlerts(Seq())


    }
  }



}
