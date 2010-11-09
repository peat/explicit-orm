package explicit.models

import explicit.db.{Factory, Persistable}
import scala.collection.mutable.HashMap
import com.mongodb.DBObject

/**
  Provides an indication of how much of a material is in a BillOfMaterials.
*/
case class Component( 
  var material: Material = Material(),
  var quantity: Long = 0 
)  extends Persistable

/**
  Provides tools for manipulating, combining, and otherwise managing Components.
*/
object Component extends Factory[Component]{

  // configure the Factory
  use("components")

  override def setObject():Component = Component()

  override def setRelations( c:Component ) = { 
    c.keys("material_id") = c.material.keys("id") 
  }

  override def setAttributes( c:Component, o:DBObject ) = {
    o.put("quantity", c.quantity)
  }

  override def setAttributes( o:DBObject, c:Component ) = {
    c.quantity = o.get("quantity").asInstanceOf[Long]
  }

  def forMaterial( m:Material ):List[Component] = {
    getMany("material_id", m.keys("id") )
  }

}
