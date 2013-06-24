package de.piddy87.actors.messages

import java.net.InetAddress

sealed trait LanShareMessage 

case class AddAdress(adress:InetAddress) extends LanShareMessage

case class RemoveAdress(adress:InetAddress) extends LanShareMessage

case object Adresses extends LanShareMessage

case class Adresses(adresses:Set[InetAddress]) extends LanShareMessage

case object Ping extends LanShareMessage

case object Pong extends LanShareMessage