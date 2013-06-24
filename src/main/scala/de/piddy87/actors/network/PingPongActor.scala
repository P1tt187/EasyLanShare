package de.piddy87.actors.network

import akka.actor.Actor
import akka.pattern.ask
import akka.event.Logging
import de.piddy87.main.EasyLanShareApp
import java.util.concurrent.TimeUnit
import de.piddy87.actors.messages.Adresses
import akka.util.Timeout
import scala.concurrent.Await

class PingPongActor extends Actor {

  private val log = Logging(context.system, this)
  private val actorRegisty = context.actorFor(EasyLanShareApp.ACTOR_REGISTRY_ACTOR_ADRESS)
  implicit val timeout = Timeout(5,TimeUnit.SECONDS)

  override def preStart {
    log.debug("Starting")
    self ! NextStep
  }

  def receive = {
    case NextStep =>
      Thread.sleep(TimeUnit.MINUTES.toMillis(1))
      Await.result(actorRegisty ? (Adresses),timeout.duration) match {
        case Adresses(adresses) => adresses.par.map{
          element =>
            val ipAdress = element.getHostName()
        //  Await.result(, atMost)
        }
      }
    		  
      
      
       
    case _ =>
  }

}

case object NextStep