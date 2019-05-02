package uk.gov.hmrc.alertconfig.builders

import spray.json.{JsArray, JsObject, JsString, JsValue}

sealed trait Severity {
  override def toString: String = this.getClass.getSimpleName.toLowerCase.replace("$", "")
}
object Ok extends Severity
object Warning extends Severity
object Critical extends Severity
object Unknown extends Severity

sealed trait Environment {
  override def toString: String = s"aws_${this.getClass.getSimpleName.toLowerCase.replace("$", "")}"
}
object Integration extends Environment
object Development extends Environment
object Qa extends Environment
object Staging extends Environment
object ExternalTest extends Environment
object Management extends Environment
object Production extends Environment

case class EnvironmentAlertBuilder(handlerName:String, enabledEnvironments: Map[Environment, Set[Severity]] = Map((Production, Set(Ok, Warning, Critical)))) {
  private val defaultSeverities: Set[Severity] = Set(Ok, Warning, Critical)
  def inIntegration(severities: Set[Severity] = defaultSeverities): EnvironmentAlertBuilder = this.copy(enabledEnvironments = enabledEnvironments + (Integration -> severities))
  def inDevelopment(severities: Set[Severity] = defaultSeverities): EnvironmentAlertBuilder = this.copy(enabledEnvironments = enabledEnvironments + (Development -> severities))
  def inQa(severities: Set[Severity] = defaultSeverities): EnvironmentAlertBuilder = this.copy(enabledEnvironments = enabledEnvironments + (Qa -> severities))
  def inStaging(severities: Set[Severity] = defaultSeverities): EnvironmentAlertBuilder = this.copy(enabledEnvironments = enabledEnvironments + (Staging -> severities))
  def inExternalTest(severities: Set[Severity] = defaultSeverities): EnvironmentAlertBuilder = this.copy(enabledEnvironments = enabledEnvironments + (ExternalTest -> severities))
  def inManagement(severities: Set[Severity] = defaultSeverities): EnvironmentAlertBuilder = this.copy(enabledEnvironments = enabledEnvironments + (Management -> severities))
  def inProduction(severities: Set[Severity] = defaultSeverities): EnvironmentAlertBuilder = this.copy(enabledEnvironments = enabledEnvironments + (Production -> severities))

  def alertConfigFor(environment: Environment): JsValue =
    JsObject(handlerName ->
      JsObject(
        "command" -> commandFor(environment),
        "type" -> JsString("pipe"),
        "severities" ->  severitiesFor(environment),
        "filter" -> JsString("occurrences")))

  private def commandFor(environment: Environment): JsValue =
    if (enabledEnvironments.contains(environment))
      JsString(s"/etc/sensu/handlers/hmrc_pagerduty_multiteam_env.rb --team iprights -e $environment")
    else
      JsString("/etc/sensu/handlers/noop.rb")

  private def severitiesFor(environment: Environment) =
    JsArray(enabledEnvironments.getOrElse(environment, defaultSeverities).map(s => JsString(s.toString)).toVector)

}
