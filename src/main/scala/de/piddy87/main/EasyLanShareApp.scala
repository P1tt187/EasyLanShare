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

  private var config = ConfigFactory.load()
  private var host = ""

  //actorSystem.logConfiguration
  NetworkInterface.getNetworkInterfaces().filter(!_.isLoopback()).foreach {
    element =>
      val ip4Adresses = element.getInetAddresses().filter(_.isInstanceOf[Inet4Address])
      ip4Adresses.foreach { adress =>
        println("akka.remote.netty.hostname=" + "\"" + adress.getHostName() + "\"")
        config = ConfigFactory.parseString("akka.remote.netty.hostname=" + "\"" + adress.getHostName() + "\"").withFallback(ConfigFactory.load())
        host = adress.getHostName()
      }
  }
  private val actorSystem = ActorSystem(ACTOR_SYSTEM_NAME, config.getConfig("akka").withFallback(config))
  private val adressRegistry = actorSystem.actorOf(Props[AdressRegistryActor], ADRESS_REGISTRY_NAME)
  actorSystem.actorOf(Props[PingPongActor].withDeploy(Deploy(scope = RemoteScope(Address("akka", ACTOR_SYSTEM_NAME, host, PingPongActor.DEFAULT_REMOTE_PORT)))), PingPongActor.PING_PONG_ACTOR_NAME)

  actorSystem.actorOf(Props[NetworkGreetActor])
  //println(adressRegistry.toString)

  // actorSystem shutdown

}