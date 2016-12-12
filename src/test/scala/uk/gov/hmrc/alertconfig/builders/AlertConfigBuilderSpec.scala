/*
 * Copyright 2016 HM Revenue & Customs
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

import java.io.FileNotFoundException

import org.scalatest.{Assertions, BeforeAndAfterEach, Matchers, WordSpec}
import spray.json._

class AlertConfigBuilderSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  override def beforeEach() {
    System.setProperty("app-config-path", "src/test/resources/app-config")
  }

  "AlertConfigBuilder" should {
    "build correct config" in  {

      val config = AlertConfigBuilder("service1", handlers = Seq("h1","h2")).build.get.parseJson.asJsObject.fields

      config("app") shouldBe JsString("service1.example.zone")
      config("handlers") shouldBe JsArray(JsString("h1"), JsString("h2"))
      config("exception-threshold") shouldBe JsNumber(2)
      config("5xx-threshold") shouldBe JsNumber(2)
      config("5xx-percent-threshold") shouldBe JsNumber(100)

    }

    "throw exception and stop processing when app config directory not found" in {
      System.setProperty("app-config-path", "this-directory-does-not-exist")

      intercept[FileNotFoundException] {
        val config = AlertConfigBuilder("service1", handlers = Seq("h1","h2")).build.get.parseJson.asJsObject.fields
      }
    }

    "Returns None when app config file not found" in {
        AlertConfigBuilder("absent-service", handlers = Seq("h1","h2")).build shouldBe None
    }

    "Returns None when app config file exists but zone key is absent" in {
      AlertConfigBuilder("service-with-absent-zone-key", handlers = Seq("h1","h2")).build shouldBe None
    }
  }


}
