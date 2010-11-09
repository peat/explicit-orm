package explicit.web

import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._

trait Jsonable { 
  def jsonData = Map("unknown" -> "unknown")
}
