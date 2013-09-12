package de.piddy87.actors.network

import akka.actor.Actor
import akka.actor.Actor._
import de.piddy87.main.EasyLanShareApp
import java.net.MulticastSocket
import java.net.InetAddress
import java.net.DatagramPacket
import akka.actor.PoisonPill
import java.net.DatagramSocket
import akka.actor.ActorPath
import de.piddy87.actors.local.AdressRegistryActor
import de.piddy87.actors.messages.AddAdress
import java.net.NetworkInterface
import scala.collection.JavaConversions._
import java.net.Inet4Address
import akka.event.Logging

class NetworkGreetActor extends Actor {

  private val DEFAULT_PORT = 5000

  private val PING_MESSAGE = "ping"

  private val PONG_MESSAGE = "pong"

  private val MULTICAST_ADRESS = InetAddress.getByName("224.0.0.1")

  private val log = Logging(context.system, this)

  override def preStart {
    log.debug("starting")
    self ! StartGreet
  }

  def receive = {
    case StartGreet =>
      log.debug(StartGreet.toString)
      greet
    case PoisonPill =>
      Thread.currentThread().interrupt()
      context.stop(self)
    case _ =>
  }

  private def greet {

    try {
      val buf = new Array[Byte](1024)

      val datagramSocket = new DatagramSocket

      val data = new DatagramPacket(PING_MESSAGE.getBytes, PING_MESSAGE.getBytes.length, MULTICAST_ADRESS, DEFAULT_PORT)

      datagramSocket.send(data)
      datagramSocket.close()
      val multicastSocket = new MulticastSocket(DEFAULT_PORT)
      multicastSocket.joinGroup(MULTICAST_ADRESS)

      while (!Thread.interrupted()) {

        val response = new DatagramPacket(buf, buf.length)
        multicastSocket.receive(response)
        val responseString = new String(response.getData(), 0, response.getLength())

        responseString match {
          case PING_MESSAGE =>
            val pong = new DatagramPacket(PONG_MESSAGE.getBytes(), PONG_MESSAGE.getBytes().length, response.getAddress(), DEFAULT_PORT)
            val socket = new DatagramSocket()
            socket send (pong)

            socket.close

          case PONG_MESSAGE =>
        }

        log.debug("greetings from: " + response.getAddress())

        context.actorFor(EasyLanShareApp.ACTOR_REGISTRY_ACTOR_ADRESS) ! AddAdress(response.getAddress())

      }
    } catch {
      case t: Throwable =>
        t.printStackTrace()
        self ! StartGreet
    }

  }

}

case object StartGreet

