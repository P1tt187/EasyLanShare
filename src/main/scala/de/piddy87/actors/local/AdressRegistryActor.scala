package de.piddy87.actors.local

import akka.actor.Actor
import java.net.InetAddress
import de.piddy87.actors.messages.LanShareMessage
import de.piddy87.actors.messages.AddAdress
import de.piddy87.actors.messages.RemoveAdress

class AdressRegistryActor extends Actor {

  private var adresses: Set[InetAddress] = Set[InetAddress]()

  override def preStart {
    println(self.path)
  }
  
  def receive = {
    case m: LanShareMessage =>
      m match {
        case AddAdress(adress) =>
          adresses += adress
          println("add adress: " + adress)
        case RemoveAdress(adress) => adresses -= adress
      }
    case _ =>
  }

}