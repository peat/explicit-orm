package explicit.models

import com.mongodb.DBObject

import explicit.db.{Factory, Persistable}
import explicit.web.Jsonable

case class Material(
  var name: String = "",
  var partNumber: String = "",
  var components:List[Component] = List()
) extends Persistable with Jsonable {

  override def jsonData = 
    Map( 
      "id" -> keys("id"), 
      "name" -> name,
      "part_number" -> partNumber
    )

}

object Material extends Factory[Material] {

  // set the data store
  use("materials")

  override def setObject():Material = Material()

  override def setAttributes( m:Material, o:DBObject ) = {
    o.put( "name", m.name )
    o.put( "partNumber", m.partNumber )
  }

  override def setAttributes( o:DBObject, m:Material ) = {
    m.name = o.get("name").asInstanceOf[String]
    m.partNumber = o.get("partNumber").asInstanceOf[String]
  }

}
