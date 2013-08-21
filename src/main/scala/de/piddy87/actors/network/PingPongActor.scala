package de.piddy87.actors.network

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import akka.actor.Actor
import akka.actor.actorRef2Scala
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import de.piddy87.actors.messages.Adresses
import de.piddy87.actors.messages.Ping
import de.piddy87.actors.messages.Pong
import de.piddy87.actors.messages.RemoveAdress
import de.piddy87.main.EasyLanShareApp
import java.util.concurrent.TimeoutException
/**
 * @author fabian
 * This class looks, if a host is lost
 */
object PingPongActor {

  val PING_PONG_ACTOR_NAME = "PingPongActor"

  val DEFAULT_REMOTE_PORT = 2552

}

class PingPongActor extends Actor {

  private val log = Logging(context.system, this)
  private val actorRegisty = context.actorFor(EasyLanShareApp.ACTOR_REGISTRY_ACTOR_ADRESS)
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  override def preStart {
    log.debug("Starting")
    self ! NextStep
  }

  def receive = {
    case NextStep =>
      Thread.sleep(TimeUnit.MINUTES.toMillis(1))
      Await.result(actorRegisty ? (Adresses), timeout.duration) match {
        case Adresses(adresses) => adresses.par.foreach {
          element =>
            val ipAdress = element.getHostName()

            val lookup = "akka://" +
              EasyLanShareApp.ACTOR_SYSTEM_NAME + "@" + ipAdress + ":" +
              PingPongActor.DEFAULT_REMOTE_PORT + "/user/" + PingPongActor.PING_PONG_ACTOR_NAME

            log.debug(lookup)

            val selection = context.actorFor(lookup)
            try {
              Await.result(selection ? Ping, timeout.duration) match {
                case Pong =>

              }
            } catch {
              case t: TimeoutException =>
                log.debug("Timeout for " + t.getMessage())
                actorRegisty ! RemoveAdress(element)
               
            }
        }
        case Timeout => log.error("uh oh, timeout")
      }
      self ! NextStep
    case Ping =>
      sender ! Pong
      log.debug("ping from " + sender.path)

    case _ =>
  }

}

case object NextStep