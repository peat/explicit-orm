package explicit.models

import explicit.db._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class MaterialTest extends Spec with ShouldMatchers with BeforeAndAfterAll {

  Database.useTest

  override def beforeAll() { Material.deleteAll() }
  override def afterAll() { Material.deleteAll() }

  describe("A Material") {
    it("should have the proper attribute types") {

      // test compilation
      Material(
        // name: String
        name = "Bits",

        // components: List[Component]
        components = List( Fixtures.component, Fixtures.component ),

        // partNumber: String
        partNumber = "1234"
      )

      // test defaults
      val r = Material()
      r.name should be ("")
      r.components should be (List[Component]())
      r.partNumber should be ("")
    }


  }

  describe("The Material factory") {
    it("should CRUD Materials") {
      // check for creation and equivalency
      val p1 = Material.save( Material( name = "foo" ) ).get
      var p2 = Material.get( p1.keys("id") ).get
      p1 should be (p2)

      // check update
      p1.name = "bar"
      Material.save( p1 )
      p2 = Material.get( p1.keys("id") ).get
      p1 should be (p2)

      // check deletion
      Material.delete( p1.keys("id") )
      Material.get( p1.keys("id") ) should be (None)
    }

  }

}
