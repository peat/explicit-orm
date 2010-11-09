package explicit.db

import scala.collection.mutable.HashMap

trait Persistable { 
  val keys:HashMap[String,String] = HashMap( "id" -> "" )
}
