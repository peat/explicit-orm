package explicit.web

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class MainRouterTest extends Spec with ShouldMatchers with BeforeAndAfterAll {

  describe("The MainRouter") {
    it("should ensure the 'action' parameter is present") {}
    it("should validate that a 'resource' is always present") {}
    it("should create an immutable hash of parameters for a request") {}
  }

  describe("A Parameter") {

    it("should report an error and no value if an error is present") {
      val p = new Param( name = "foo", error = Some("busted up!") )  
      p.hasError should be (true)
      p.value should be (None)
    }
  }

}
