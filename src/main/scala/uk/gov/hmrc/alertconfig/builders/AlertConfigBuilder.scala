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

import spray.json.DefaultJsonProtocol._

trait Builder[T] {
  def build: T
}

case class AlertConfigBuilder(serviceName: String, handlers: Seq[String] = Seq("noop"), exceptionThreshold: Int = 2, http5xxThreshold: Int = 2, http5xxPercentThreshold: Double = 100) extends Builder[String]{

  import spray.json._

  def withHandlers(handlers: String*) = this.copy(handlers = handlers)

  def withExceptionThreshold(exceptionThreshold: Int) = this.copy(exceptionThreshold = exceptionThreshold)

  def withHttp5xxThreshold(http5xxThreshold: Int) = this.copy(http5xxThreshold = http5xxThreshold)

  def withHttp5xxPercentThreshold(http5xxPercentThreshold: Int) = this.copy(http5xxPercentThreshold = http5xxPercentThreshold)

  def build: String =
  s"""
     |{\"app\": \"${serviceName}.service\",\"handlers\": ${handlers.toJson.compactPrint}, \"exception-threshold\":${exceptionThreshold}, \"5xx-threshold\":${http5xxThreshold}, \"5xx-percent-threshold\":${http5xxPercentThreshold}}
    """.stripMargin

}

case class TeamAlertConfigBuilder(services: Seq[String], handlers: Seq[String] = Seq("noop"), exceptionThreshold: Int = 2, http5xxThreshold: Int = 2, http5xxPercentThreshold: Double = 100) extends Builder[Seq[AlertConfigBuilder]] {

  def withHandlers(handlers: String*) = this.copy(handlers = handlers)

  def withExceptionThreshold(exceptionThreshold: Int) = this.copy(exceptionThreshold = exceptionThreshold)

  def withHttp5xxThreshold(http5xxThreshold: Int) = this.copy(http5xxThreshold = http5xxThreshold)

  def withHttp5xxPercentThreshold(percentThreshold: Double) = this.copy(http5xxPercentThreshold = percentThreshold)

  override def build: Seq[AlertConfigBuilder] = services.map(service =>
    AlertConfigBuilder(service, handlers, exceptionThreshold, http5xxThreshold, http5xxPercentThreshold)
  )
}

object TeamAlertConfigBuilder {

  def teamAlerts(services: Seq[String]): TeamAlertConfigBuilder = {
    require(services.nonEmpty, "no alert service provided")
    TeamAlertConfigBuilder(services)
  }

}
