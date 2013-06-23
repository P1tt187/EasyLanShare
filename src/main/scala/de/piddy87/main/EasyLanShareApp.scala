/**
 *
 */
package de.piddy87.main

import akka.actor.ActorSystem
import akka.actor.Props
import de.piddy87.actors.local.AdressRegistryActor
import de.piddy87.actors.network.NetworkGreetActor
import com.typesafe.config.ConfigFactory

/**
 * @author fabian
 *
 */
object EasyLanShareApp extends App {
  
  val config = ConfigFactory.load()

  val actorSystem = ActorSystem("ShareSystem",config.getConfig("akka").withFallback(config))
  //actorSystem.logConfiguration

  val adressRegistry = actorSystem.actorOf(Props[AdressRegistryActor],"AdressRegistryActor")
  actorSystem.actorOf(Props[NetworkGreetActor])
  //println(adressRegistry.toString)

 // actorSystem shutdown

}