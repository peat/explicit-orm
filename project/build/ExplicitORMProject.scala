import sbt._

class ExplicitORMProject( info: ProjectInfo ) extends DefaultWebProject(info)
{
  val scalaToolsSnapshots = ScalaToolsSnapshots

  val scalaTest = "org.scalatest" % "scalatest" % "1.2-for-scala-2.8.0.RC6-SNAPSHOT" % "test"

  val mongoDb = "org.mongodb" % "mongo-java-driver" % "1.4"
  // val mongoScalaDriver = "com.osinka" % "mongo-scala-driver_2.8.0.RC7" % "0.8.1"

  val baseServlet = "javax.servlet" % "servlet-api" % "2.5"
  val jettyWebapp = "org.eclipse.jetty" % "jetty-webapp" % "7.0.2.RC0" % "test" 
  val jettyServer = "org.eclipse.jetty" % "jetty-server" % "7.0.2.RC0" % "test"

  val circumflexCore = "ru.circumflex" % "circumflex-core" % "1.0"

  val liftJson = "net.liftweb" % "lift-json" % "2.0-scala280-SNAPSHOT"
}
