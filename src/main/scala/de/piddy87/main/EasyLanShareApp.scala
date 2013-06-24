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

  val ADRESS_REGISTRY_NAME = "AdressRegistryActor"
  val ACTOR_SYSTEM_NAME = "ShareSystem"
  val ACTOR_REGISTRY_ACTOR_ADRESS = "akka://" + ACTOR_SYSTEM_NAME + "/user/" + ADRESS_REGISTRY_NAME

  private val config = ConfigFactory.load()

  private val actorSystem = ActorSystem(ACTOR_SYSTEM_NAME, config.getConfig("akka").withFallback(config))
  //actorSystem.logConfiguration

  private val adressRegistry = actorSystem.actorOf(Props[AdressRegistryActor], ADRESS_REGISTRY_NAME)
  actorSystem.actorOf(Props[NetworkGreetActor])
  //println(adressRegistry.toString)

  // actorSystem shutdown

}