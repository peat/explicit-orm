package explicit.models

import explicit.web._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class JsonTest extends Spec with ShouldMatchers with BeforeAndAfterAll {

  describe("The Json object") {
    it("should convert a Material to JSON") {
      val p1 = Material( name = "foo", partNumber = "1234" )
      Json.from( p1 ) should be ("{\"jsonClass\":\"Material\",\"name\":\"foo\",\"part_number\":\"1234\"}")
    }

    it("should convert a list of Materials to JSON") {
      val p1 = Material( name = "foo", partNumber = "1234" )
      val p2 = Material( name = "bar", partNumber = "4321" )

      Json.from( List(p1,p2) ) should be ("[{\"jsonClass\":\"Material\",\"name\":\"foo\",\"part_number\":\"1234\"},{\"jsonClass\":\"Material\",\"name\":\"bar\",\"part_number\":\"4321\"}]")
    }
  }

}
