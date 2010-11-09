package explicit.db

import com.mongodb._

object Database {

  val connection = new Mongo("localhost")
  var database = connection.getDB("test")

  def useTest() = { database = connection.getDB("test") }
  def useProduction() = { database = connection.getDB("production") }

  def getCollection( c:String ):DBCollection = { database.getCollection(c) }
  def dropCollection( c:String ) { database.getCollection(c).drop }

}
