package explicit.web

import scala.collection._
import ru.circumflex.core._
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._

class MainRouter extends RequestRouter {

  get("/") = {

    val paramWhitelist = List(
      Param( name = "action", error = Some("Required for all requests.") ),
      Param( name = "callback" ),
      Param( name = "access_token" )
    )

    val cleanedParams = getParams( paramWhitelist )
    val paramErrors = cleanedParams.filter( p => { p.hasError } )

    val json:JsonAST.JValue = paramErrors.isEmpty match {
      case true => {
        ("received" -> cleanedParams.map { p => { ("name" -> p.name) ~ ("value" -> p.value.get ) } } )
      }
      case false => {
        ("errors" -> paramErrors.map { p => { ("name" -> p.name) ~ ("error" -> p.error.get) } } )
      }
    }

    compact(JsonAST.render(json))
  }

  def getParams( list:List[Param] ):List[Param] = {
    list
      .map( 
        p => {
          // param() is the Circumflex method for pulling param values!
          (param( p.name ), p.error) match {
            case (None, None)  => None // no data, no error trigger
            case (None, error) => Some( Param( name = p.name, error = error ) ) // no data, an error message
            case (value, _ )   => Some( Param( name = p.name, value = value ) ) // data!
          } 
        } // Option[Param]
      ) // List[Option[Param]]
      .filter( op => { op.isDefined } ) // remove Nones
      .map( op => { op.get } ) // List[Param]
  }

}

case class Param( 
  val name:String, 
  val value:Option[String] = None, 
  val error:Option[String] = None
) {
  override def toString = "Param( name:" + name + ", value:" + value + ", error:" + error + ")"  
  def hasError = error.isDefined
}
