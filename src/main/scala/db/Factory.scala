package explicit.db

import com.mongodb.{ DBCollection, DBObject, BasicDBObject, DBCursor }
import scala.collection.mutable.{ HashMap, ArrayBuffer }
import java.util.UUID

trait Factory[T] {

  var collection:DBCollection = _
  var tableName:String = _

  /**
    Determines what MongoDB collection to store records in.
  */
  def use( name:String ):Unit = {
    this.tableName = name
    this.collection = Database.getCollection( this.tableName )
  }

  /**
    Deletes all of the records in this collection.  Use wisely!
  */
  def deleteAll():Unit = { Database.dropCollection( this.tableName ) }

  /**
    Returns a new, empty object; implement in your Factory object.
  */
  def setObject():T 

  /**
    Should update the values in the keys hash in your peristable object.
  */
  def setRelations(t:T) = {}

  /**
    Takes attributes from your Persistable object, and sticks them into the DBObject
  */
  def setAttributes(t:T, o:DBObject) = {}

  /**
    Takes attributes from your DBObject, and sticks them into your Persistable object
  */
  def setAttributes(o:DBObject, t:T) = {}


  def rehydrate( o:DBObject ):T = {
    val t = setObject()
    setKeys( o, t )
    setAttributes( o, t )
    return t
  }

  def dehydrate( t:T ):DBObject = {
    val o = new BasicDBObject()
    setRelations( t )
    setKeys( t, o )
    setAttributes( t, o )
    return o
  }

  def setKeys( from:DBObject, to:T ):Unit = {
    for( (key, value) <- to.asInstanceOf[Persistable].keys )
      to.asInstanceOf[Persistable].keys( key ) = from.get( key ).asInstanceOf[String]
  }

  def setKeys( from:T, to:DBObject ):Unit = {
    for( (key, value) <- from.asInstanceOf[Persistable].keys )
      to.put( key, value )
  }

  def get( id:String ):Option[T] = { getOne( "id", id ) }

  /**
    Stores an object in the database; generates a new object with the same
    data, and with a UUID.
  */

  def create( target:Persistable ):Option[T] = { 

    // assign a UUID
    val uuid = UUID.randomUUID().toString()
    target.keys("id") = uuid

    collection.insert( dehydrate( target.asInstanceOf[T] ) ) 
    get( uuid )
  }

  /**
    Updates a product in the database, based on the UUID
  */
  def update( target:Persistable ):Option[T] = { 
    val query = simpleQuery( "id", target.keys("id") )
    val dbObject = dehydrate( target.asInstanceOf[T] )
    collection.update( query, dbObject )
    Some( target.asInstanceOf[T] )
  }

  def delete( id:String ) = {
    val query = simpleQuery( "id", id )
    collection.remove( query )
  }

  def delete( key:String, value:String ) = {
    val query = simpleQuery( key, value )
    collection.remove( query )
  }

  /**
    Detects whether to create or update an object.
  */
  def save( target:Persistable ):Option[T] = {
    // see if one of these already exists
    val received = getOne( "id", target.keys("id") )

    if (received.isDefined)
      update(target)
    else
      create(target)
  }

  /**
    Gets a single object based on simple key:value criteria
 */
  def getOne( key:String, value:String ):Option[T] = {
    val cursor = collection.find( simpleQuery( key, value ) )
    
    if (cursor.hasNext) {
      Some( rehydrate( cursor.next ) )
    } else {
      None
    }

  }

  /**
    Gets several objects based on a simple key:value criteria
  */
  def getMany( key:String, value:String ):List[T] = {
    manyBuilder( collection.find( simpleQuery( key, value ) ) )
  }

  /**
    Gets all of the objects in the collection.
  */
  def getAll():List[T] = {
    manyBuilder( collection.find() )
  }

  /**
    Builds a DBObject representing a simple key:value query
  */
  private def simpleQuery( key:String, value:String ):DBObject = {
    val query = new BasicDBObject()
    query.put(key, value)
    query
  }

  private def manyBuilder( cursor:DBCursor ):List[T] = {
    val out:ArrayBuffer[DBObject] = new ArrayBuffer()

    while (cursor.hasNext) {
      out += cursor.next
    }

    out.toList.map( rehydrate )
  }

}
