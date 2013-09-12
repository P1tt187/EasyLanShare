package de.piddy87.actors.network

import akka.actor.Actor
import de.piddy87.actors.messages.{ Ping, Pong }

object PongActor {

  val PONG_ACTOR_NAME = "PonggActor"

  val DEFAULT_REMOTE_PORT = 2552

}

class PongActor extends Actor {

  import akka.event.Logging

  private val log = Logging(context.system, this)

  override def preStart {
    log.debug("Starting")

  }

  override def receive = {
    case Ping =>
      sender ! Pong
      log.debug("ping from " + sender.path)
    case other => log.error("unkown message" + other)
  }

}