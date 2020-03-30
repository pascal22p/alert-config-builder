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

import org.reflections.Reflections

import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._


object ObjectScanner {

  val scanPackage = {
    val pathProp = System.getProperty("scan.package")
    if (pathProp != null) pathProp else "uk.gov.hmrc.alertconfig.configs"
  }

  def loadAll[T](_package: String = scanPackage)(implicit ct: ClassTag[T]): Set[T] = {

    val objects =
      new Reflections(_package)
        .getSubTypesOf[T](ct.runtimeClass.asInstanceOf[Class[T]]).asScala.toSet

    objects.map(x => objectInstance[T](x.getName))

  }

  def objectInstance[T](name: String) = {
    val mirror = runtimeMirror(getClass.getClassLoader)
    val module = mirror.staticModule(name)
    mirror.reflectModule(module).instance.asInstanceOf[T]
  }


}
