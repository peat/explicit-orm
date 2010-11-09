package explicit.models

import explicit.db._
import com.mongodb._
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

case class Simple( 
  var name:String = "hello",
  var age:Long = 123,
  var belongsTo:Simple = null // prevents recusive overflow
) extends Persistable

object SimpleFactory extends Factory[Simple] {

  use("simple")

  override def setObject():Simple = Simple()

  override def setAttributes( in:Simple, out:DBObject ) = { 
    out.put("name", in.name) 
    out.put("age", in.age) 
  }

  override def setAttributes( in:DBObject, out:Simple ) = {
    out.name = in.get("name").asInstanceOf[String]
    out.age = in.get("age").asInstanceOf[Long]
  }

  override def setRelations( s:Simple ) = {
    if (s.belongsTo != null) s.keys("parent_id") = s.belongsTo.keys("id")
  }

}

class FactoryTest extends Spec with ShouldMatchers with BeforeAndAfterEach with BeforeAndAfterAll {

  Database.useTest
  override def beforeEach() { SimpleFactory.deleteAll() }
  override def afterAll() { SimpleFactory.deleteAll() }

  describe("A Factory") {
    it("should create and retrieve an object from the collection") {
      val in = SimpleFactory.create( Simple() ).get 
      val out = SimpleFactory.get( in.keys("id") ).get

      in should be (out)
    }

    it("should update an object in the collection") {
      val in = SimpleFactory.create( Simple() ).get 
      in.name = "different"
      val out = SimpleFactory.update( in ).get

      in should be (out)
      in.name should be ("different")
      out.name should be ("different")
    }

    it("should save all of the attributes provided for the object") {
      val in = SimpleFactory.create( Simple( name = "foo", age = 31 ) ).get

      in.name should be ("foo")
      in.age should be (31)
    }

    it("should update the attributes provided for the object") {
      val in = SimpleFactory.create( Simple( name = "foo", age = 31 ) ).get

      in.name = "bar"
      in.age = 21

      val out = SimpleFactory.update( in ).get

      out.name should be ("bar")
      out.age should be (21)
    }

    it("should delete an object from the collection based on a primary key") {
      val in = SimpleFactory.create( Simple( name = "foo", age = 31 ) ).get

      SimpleFactory.delete( in.keys("id") )

      val out = SimpleFactory.get( in.keys("id") )

      out should be (None)
    }

    it("should delete an object from the collection based on key:value pair") {
      val in = SimpleFactory.create( Simple( name = "foo", age = 31 ) ).get

      SimpleFactory.delete( "name", "foo" )

      val out = SimpleFactory.get( in.keys("id") )

      out should be (None)
    }

    it("should delete all objects in the collection") {
      val in = SimpleFactory.create( Simple( name = "foo", age = 31 ) ).get
       
      SimpleFactory.deleteAll()
      SimpleFactory.getAll.length should be(0)
    }

    it("should provide an agnostic create/update method") {
      val created = SimpleFactory.save( Simple() ).get

      created should be (Simple())

      created.name = "changed"
      
      val updated = SimpleFactory.save( created ).get

      updated should be (created)
    }

    it("should get an object in a collection, based on it's primary key") {
      val in = SimpleFactory.save( Simple() ).get 

      val pkey = in.keys("id")

      val out = SimpleFactory.get( pkey ).get

      out.keys("id") should be (pkey)
      out should be (in)
    }

    it("should get an object in a collection, based on a key:value pair") {
      val in = SimpleFactory.save( Simple() ).get 
      val out = SimpleFactory.getOne( key = "name", value = Simple().name ).get

      in.name should be (out.name)
      in should be (out)
    }

    it("should get an object in a collection, based on it's foreign key") {
      val p = SimpleFactory.save( Simple( name = "parent" ) ).get
      val c = SimpleFactory.save( Simple( name = "child", belongsTo = p ) ).get

      val out = SimpleFactory.getOne( "parent_id", p.keys("id") ).get

      out should be (c)
      out.name should be ("child")
    }


    it("should get several objects in a collection, based on a foreign key") {
      val p = SimpleFactory.save( Simple( name = "parent" ) ).get
      val c1 = SimpleFactory.save( Simple( name = "child 1", belongsTo = p ) ).get
      val c2 = SimpleFactory.save( Simple( name = "child 2", belongsTo = p ) ).get
      val c3 = SimpleFactory.save( Simple( name = "child 2", belongsTo = p ) ).get

      val out = SimpleFactory.getMany( "parent_id", p.keys("id") )

      out.length should be (3)
    }

    it("should get all objects in a collection") {
      SimpleFactory.save( Simple() )
      SimpleFactory.save( Simple() )
      SimpleFactory.save( Simple() )
      SimpleFactory.save( Simple() )
      SimpleFactory.save( Simple() )
    
      SimpleFactory.getAll.length should be (5)
    }

    it("should return an Option[T] when fetching single records") {
      val in = SimpleFactory.save( Simple() ).get
      val some = SimpleFactory.get( in.keys("id") )

      some should be ( Some(in) )

      val none = SimpleFactory.get( "123" ) // bogus ID
      none should be (None)
    }

    it("should return a List[T] when fetching multiple records") {
      SimpleFactory.getAll should be (List[Simple]())

      SimpleFactory.save( Simple() )

      SimpleFactory.getAll should be (List(Simple()))
    }
  }


}
