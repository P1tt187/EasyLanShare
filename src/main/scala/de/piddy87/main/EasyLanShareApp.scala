/**
 *
 */
package de.piddy87.main

import akka.actor.ActorSystem
import akka.actor.Props
import de.piddy87.actors.local.AdressRegistryActor
import de.piddy87.actors.network.NetworkGreetActor
import com.typesafe.config.ConfigFactory
import de.piddy87.actors.network.PingPongActor
import de.piddy87.actors.network.PingPongActor
import akka.actor.{ Props, Deploy, Address, AddressFromURIString }
import akka.remote.RemoteScope
import akka.actor.Deploy
import akka.remote.RemoteScope

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
  actorSystem.actorOf(Props[PingPongActor].withDeploy(Deploy(scope = RemoteScope(Address("akka", ACTOR_SYSTEM_NAME, "127.0.0.1", PingPongActor.DEFAULT_REMOTE_PORT)))), PingPongActor.PING_PONG_ACTOR_NAME)
  actorSystem.actorOf(Props[NetworkGreetActor])
  //println(adressRegistry.toString)

  // actorSystem shutdown

}