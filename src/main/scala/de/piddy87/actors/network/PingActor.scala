package de.piddy87.actors.network

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import akka.actor.Actor
import akka.actor.actorRef2Scala
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import de.piddy87.actors.messages.{Adresses,Ping,Pong,RemoveAdress}
import de.piddy87.main.EasyLanShareApp
import java.util.concurrent.TimeoutException
import java.net.ConnectException
/**
 * @author fabian
 * This class looks, if a host is lost
 */


class PingActor extends Actor {

  private val log = Logging(context.system, PingActor.this)
  private val actorRegisty = context.actorFor(EasyLanShareApp.ACTOR_REGISTRY_ACTOR_ADRESS)
  implicit val timeout = Timeout(15, TimeUnit.SECONDS)

  override def preStart {
    log.debug("Starting")
    self ! NextStep
  }

  def receive = {
    case NextStep =>
      Thread.sleep(TimeUnit.SECONDS.toMillis(10))
      log.debug("ask for adresses")
      Await.result(actorRegisty ? (Adresses), timeout.duration) match {
        case Adresses(adresses) =>
          log.debug("got adresses " + adresses)
          adresses.par.foreach {
            element =>
            val ipAdress = element.getHostName()

            val lookup = "akka://" +
              EasyLanShareApp.ACTOR_SYSTEM_NAME + "@" + ipAdress + ":" +
              PongActor.DEFAULT_REMOTE_PORT + "/user/" + PongActor.PONG_ACTOR_NAME

            log.debug(lookup)

            val selection = context.actorFor(lookup)
            try {
              Await.result(selection ? Ping, timeout.duration) match {
                case Pong =>

              }
            } catch {
              case t: TimeoutException =>
                log.error("Timeout for " + t.getMessage())
                actorRegisty ! RemoveAdress(element)
              case e: ConnectException =>
                log.error("unable to connect to" + e.getMessage())
                actorRegisty ! RemoveAdress(element)
            }
          }
        case Timeout => log.error("uh oh, timeout")
      }
      self ! NextStep
    case other => log.error("unkown message" + other)

    
  }

}

case object NextStep