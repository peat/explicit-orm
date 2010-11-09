package explicit.models

import explicit.db._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class ComponentTest extends Spec with ShouldMatchers with BeforeAndAfterAll {

  Database.useTest
  override def beforeAll() { Component.deleteAll() }
  override def afterAll() { Component.deleteAll() }

  describe("A Component") {
    it("should have the proper constructor types") {

      // compilation test to ensure types are right
      Component(
        material = Fixtures.material,
        quantity = 25
      )

      // test defaults
      val c = Component()
      c.material should be (Material())
      c.quantity should be (0)
    }

  }

  describe("The Component factory") {

    it("should CRUD Components") {
      // check for creation and equivalency
      val p1 = Component.save( Component( quantity = 100 ) ).get
      var p2 = Component.get( p1.keys("id") ).get
      p1 should be (p2)

      // check update
      p1.quantity = 200 
      Component.save( p1 )
      p2 = Component.get( p1.keys("id") ).get
      p1 should be (p2)

      // check deletion
      Component.delete( p1.keys("id") )
      Component.get( p1.keys("id") ) should be (None)
    }

    it("should provide a set of Components for a given Material") {

      val b = Material.save( Material( name = "woot" ) ).get

      Component.save( Component( quantity = 100, material = b ) )
      Component.save( Component( quantity = 200, material = b ) )
      Component.save( Component( quantity = 300, material = b ) )

      val ps = Component.forMaterial( b )

      ps.length should be (3)
    }

  }

}
