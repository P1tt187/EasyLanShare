/**
 *
 */
package de.piddy87.main

import akka.actor.ActorSystem
import akka.actor.Props
import de.piddy87.actors.local.AdressRegistryActor
import de.piddy87.actors.network.NetworkGreetActor

/**
 * @author fabian
 *
 */
object EasyLanShareApp extends App {

  val actorSystem = ActorSystem("ShareSystem")

  val adressRegistry = actorSystem.actorOf(Props[AdressRegistryActor],"AdressRegistryActor")
  actorSystem.actorOf(Props[NetworkGreetActor])
  //println(adressRegistry.toString)

 // actorSystem shutdown

}