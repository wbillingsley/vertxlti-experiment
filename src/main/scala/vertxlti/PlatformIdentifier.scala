package vertxlti

case class BadRequest(msg:String) extends Throwable

/** An LTI 1.3 Login request is an OpenID Connect login request with additional parameters */
case class Lti13Login(
  iss:String, loginHint:Option[String], targetLinkUri:Option[String],
  ltiMessageHint:Option[String], ltiDeploymentId:Option[String], clientId:Option[String]
)

/** 
 * Key elements from the LTI Advantage Dynamic Registration protocol.
 * From https://moodlelti.theedtech.dev/dynreg/
 */
case class PlatformOpenIdConfigRegElements(
  issuer: String, 
  tokenEndPoint: String, 
  tokenEndPointAuthMethodsSupported: Seq[String] = Seq.empty,
  tokenEndPointAuthSigningAlgValuesSupp: Seq[String] = Seq.empty,
  jwksUri: Option[String] = None,
  authorizationEndPoint: Option[String] = None,
  registrationEndPoint: Option[String] = None,
  scopesSupported: Seq[String] = Seq.empty,                        
   // We don't worry about the ltiPlatformConfig part                              
)

case class PlatformIdentifier(clientId:String, deploymentId:String, publicKey:String) {

}
