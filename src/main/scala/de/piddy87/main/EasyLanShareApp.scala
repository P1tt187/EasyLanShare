/**
 *
 */
package de.piddy87.main

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

import scala.collection.JavaConversions.enumerationAsScalaIterator

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Address
import akka.actor.Deploy
import akka.actor.Props
import akka.remote.RemoteScope
import de.piddy87.actors.local.AdressRegistryActor
import de.piddy87.actors.network.NetworkGreetActor
import de.piddy87.actors.network.PingPongActor

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
  NetworkInterface.getNetworkInterfaces().foreach {
    element =>
      if (!element.isLoopback() && element.isInstanceOf[Inet4Address]) {
        ActorSystem("akka.remote.netty.hostname=" + element.getInetAddresses().nextElement().getHostName())
      }

  }
  private val adressRegistry = actorSystem.actorOf(Props[AdressRegistryActor], ADRESS_REGISTRY_NAME)
  actorSystem.actorOf(Props[PingPongActor].withDeploy(Deploy(scope = RemoteScope(Address("akka", ACTOR_SYSTEM_NAME, InetAddress.getLocalHost.getHostAddress, PingPongActor.DEFAULT_REMOTE_PORT)))), PingPongActor.PING_PONG_ACTOR_NAME)

  actorSystem.actorOf(Props[NetworkGreetActor])
  //println(adressRegistry.toString)

  // actorSystem shutdown

}