/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.alertconfig

import org.scalatest.{Matchers, WordSpec}


trait SuperType

case object A extends SuperType

object B extends SuperType

object C

class ObjectScannerSpec extends WordSpec with Matchers {

  "ClassScanner" should {
    "load all the singleton subtypes of a type in given package" in {

      ObjectScanner.loadAll[SuperType](this.getClass.getPackage.getName) should contain only(A, B)

    }

  }

}
