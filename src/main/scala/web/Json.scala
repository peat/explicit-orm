package explicit.web

import explicit.db._
import explicit.models._

import net.liftweb.json._
import net.liftweb.json.JsonAST._

object Json {

  val jsonableHint = new ShortTypeHints(classOf[Jsonable] :: Nil) {

    override def serialize: PartialFunction[Any, JObject] = {
      case j:Jsonable => mapJsonable( j )
    }

  }

  implicit val formats = DefaultFormats.withHints(jsonableHint)

  def mapJsonable( j:Jsonable ):JObject = {
    JObject( 
      // fetches the data from the Jsonable object ...
      j.jsonData
      
      // ... remove any values that are empty strings
      .filter(
        pair => pair._2.length > 0
      )

      // ... turn each pair into a JField object
      .map( 
        pair => JField( pair._1, JString( pair._2 ) ) 
      )
      
      // .. collect them all into a List[JField]
      .toList 
    )
  }

  def from( p:Persistable ):String = { 
    Serialization.write( p ) 
  }

  def from( l:List[Persistable] ):String = { Serialization.write( l ) }


}
