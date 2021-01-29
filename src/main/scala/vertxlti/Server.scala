package vertxlti

import io.vertx.core.http.{HttpHeaders, HttpMethod, HttpServerRequest}
import io.vertx.core.Vertx
import io.vertx.ext.web.{Router, RoutingContext}
import io.vertx.ext.web.handler.BodyHandler
import com.wbillingsley.handy._
import com.wbillingsley.handy.refOps

import scala.concurrent.{ExecutionContext, Future, Promise}


class Server(using vertx:Vertx) {

  lazy val server = vertx.createHttpServer()
  lazy val router = Router.router(vertx)
  lazy val client = vertx.createHttpClient()
  
  lazy val ltiAdvantageHandler = LTIAdvantageHandler(this)
  
  /** Handler for LTI 1.3 login requests via POST */
  def login(ctx:RoutingContext):Unit = {
    val req = ctx.request()
    
    for 
      iss <- req.refParam("iss") orFail BadRequest("No issuer")
      loginHint <- req.refParam("login_hint").option
      targetLinkUri <- req.refParam("target_link_uri").option
      
      ltiMessageHint <- req.refParam("lti_message_hint").option
      ltiDeploymentId <- req.refParam("lti_deployment_id").option
      clientId <- req.refParam("client_id").option
    do {
      val msg = Lti13Login(
        iss=iss, loginHint=loginHint, targetLinkUri=targetLinkUri,
        ltiMessageHint=ltiMessageHint, ltiDeploymentId=ltiDeploymentId, clientId=clientId
      )
      
      // asas
      ctx.response().end(s"Login received: $msg")
    }

  }
  
  def start():Future[Unit] = {
    val p = Promise[Unit]
    
    router.route().handler(BodyHandler.create())
    
    router.get("/ping").handler((ctx) => ctx.request().response().end("Pong"))    
    router.post("/lti1.3/login").handler((ctx) => login(ctx))
    router.get("/lti1.3/advantageRegistration").handler((ctx) => ltiAdvantageHandler.ltiAdvantageRegistration(ctx))
    
    router.route().handler({ ctx =>
      
      println(s"Request... ${ctx.request().method().name()} ${ctx.request().path()}")
      
      val resp = ctx.response()
      resp.putHeader("Content-type", "text/plain")
      resp.end("Ping")
    })
      
    server.requestHandler(router).listen(8080, { http =>
      if (http.succeeded()) then
        println("Server started on 8080")
        p.success(())
      else
        println("Server failed to start")
        p.failure(http.cause())
    })
    
    p.future
  }

}
