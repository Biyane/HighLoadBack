import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior }

object TempOne {
  final case class TempClass(myarr: Array[Double], str: String)
  def apply(): Behavior[TempClass] = Behaviors.receive{(context, message) =>
    val main = new Main(message.myarr, message.str)
    main.printNum()
    Behaviors.same
  }
}
