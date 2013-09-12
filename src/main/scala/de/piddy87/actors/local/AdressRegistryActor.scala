package de.piddy87.actors.local

import akka.actor.Actor
import java.net.InetAddress
import de.piddy87.actors.messages.LanShareMessage
import de.piddy87.actors.messages.AddAdress
import de.piddy87.actors.messages.RemoveAdress
import akka.event.Logging
import de.piddy87.actors.messages.Adresses
/**
 * @author fabian
 * This Actor handles all known ip adresses 
 * */
class AdressRegistryActor extends Actor {

  private var adresses: Set[InetAddress] = Set[InetAddress]()
  private val log = Logging(context.system, this)

  override def preStart {
    log.debug(self.path.toString())

  }

  def receive = {
    case m: LanShareMessage =>
      m match {
        case AddAdress(adress) =>
          adresses += adress
          log.debug("add adress: " + adress)
        case RemoveAdress(adress) =>
          log.debug("remove adress: " + adress)
          adresses -= adress

        case Adresses => sender ! Adresses(adresses)

        case other => log.error("uh oh, want " + classOf[LanShareMessage].getSimpleName + " but got " + other)
      }

  }

}