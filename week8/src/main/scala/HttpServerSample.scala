import java.io.File

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.{Logger, LoggerFactory}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import akka.routing.Router
import akka.stream.Supervision.Directive
import akka.stream.scaladsl.FileIO
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


object  MyServer{
  def startHttpServer(routes: Route)(implicit system: ActorSystem[_],  ex:ExecutionContext): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    val futureBinding = Http().newServerAt("localhost", 8082).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
}

trait  Router {
  def route:Route
}
class MyRouter(val todoRepository: TodoRepository)(implicit system: ActorSystem[_],  ex:ExecutionContext) extends  Router with  Directives{

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import  io.circe.generic.auto._

  var myArray: Array[String] = Array.empty
  case class Video(file: File, title: String, author: String)
  object db {
    def create(video: Video): Future[Unit] = Future.successful(())
  }

  val uploadVideo = {
    concat(
    path("video") {
      entity(as[Multipart.FormData]) { formData =>

        // collect all parts of the multipart as it arrives into a map
        val allPartsF: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

          case b: BodyPart if b.name == "file" =>
            // stream into a file as the chunks of it arrives and return a future
            // file to where it got stored
//            myArray :+= b.name
            val file = File.createTempFile("upload", "tmp")
            b.entity.dataBytes.runWith(FileIO.toPath(file.toPath)).map(
              _ => b.name -> file
            )

          case b: BodyPart =>
            // collect form field values
            b.filename match {
              case Some(value) => myArray :+= value
              case None => println("None de fuk")
            }
            b.toStrict(2.seconds).map(strict =>
              (b.name -> strict.entity.data.utf8String))

        }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)
        val done = allPartsF.map { allParts =>
          // You would have some better validation/unmarshalling here
          db.create(Video(
            file = allParts("file").asInstanceOf[File],
            title = allParts("title").asInstanceOf[String],
            author = allParts("author").asInstanceOf[String]))
        }

        // when processing have finished create a response for the user
        onSuccess(allPartsF) { allParts =>
          println(allParts)
          complete {
            "ok!"
          }
        }
      }
    },
      path("files"){
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "[" + myArray.mkString(", ") + "]"))
        }
      }
    )
  }


  override def route = {
    concat(
      path("ping") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong"))
        }
      },
      pathPrefix("files") {
        pathEndOrSingleSlash {
          get {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>todos<h1>"))
            //complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
          }
        }
      },
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      })
  }
}


object HttpServerSample {

  def main(args: Array[String]): Unit = {

    implicit val log: Logger = LoggerFactory.getLogger(getClass)

    val rootBehavior = Behaviors.setup[Nothing] { context =>

      val todos:Seq[Todo] = Seq(
        Todo("1","title1","description1",true),
        Todo("2","title2","description2",false)
      )

      val todoRepository = new InMemoryTodoRepository(todos)(context.executionContext)
      val router = new MyRouter(todoRepository)(context.system, context.executionContext)

      MyServer.startHttpServer(router.uploadVideo)(context.system, context.executionContext)
      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
  }

//   def main(args: Array[String]): Unit = {
//    val log: Logger = LoggerFactory.getLogger(getClass)
//    implicit val system = ActorSystem(Behaviors.empty, "my-system")
//    implicit val executionContext = system.executionContext
//    val route = {
//      concat(
//        path("ping") {
//          get {
//            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong"))
//          }
//        },
//        path("hello") {
//          get {
//            getFromDirectory("index.html")
//            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "a"))
//          }
//        })
//    }
//     val bindingFuture = Http().newServerAt("localhost", 8081).bind(route)
//     println("hello world")
//      bindingFuture
//        .onComplete{
//          case Success(binding) =>
//            log.info(s"Server online at http://localhost:8081/\nPress RETURN to stop...${binding.localAddress}")
//          case Failure(ex) =>
//            system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
//            system.terminate()
//        }
//
//   }

}