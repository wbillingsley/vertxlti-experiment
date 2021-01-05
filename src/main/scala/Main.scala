import io.vertx.core.{AbstractVerticle, Launcher, Promise}
import vertxlti.Server

import scala.concurrent.ExecutionContext

class Main extends AbstractVerticle {
  override def start(startPromise: Promise[Void]) = {
    val server = new Server(using vertx)
    given ec:ExecutionContext = ExecutionContext.global
    for 
      started <- server.start()
    do
      println("Server is up")

  }
}

object Main {

  def main(args: Array[String]): Unit = {
    Launcher.executeCommand("run", "Main")
  }
  
}
