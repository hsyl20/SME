import sbt._
import Keys._

object MyProject extends Build {
  val buildOrganization = "fr.hsyl20"
  val buildName         = "SME"
  val buildVersion      = "0.1"
  val buildScalaVersion = "2.9.1"

  lazy val project = Project (buildName, file("."), settings = mySettings)

  val mySettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    name         := buildName,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions := Seq("-deprecation", "-unchecked"),
    resolvers    := myResolvers,
    libraryDependencies := myDependencies,

    /* LWJGL native libraries */
    fork         := true,
    javaOptions  += "-Djava.library.path=" + nativeJavaLibraryPath,

    /* GIT ready shell prompt */
    shellPrompt  := ShellPrompt.buildShellPrompt,

    /* Tasks */
    fullRunTask(TaskKey[Unit]("run-hello-simple-application"), Test, "fr.hsyl20.sme.tutorial.HelloSimpleApplication"),
    fullRunTask(TaskKey[Unit]("run-hello-node"), Test, "fr.hsyl20.sme.tutorial.HelloNode"),
    fullRunTask(TaskKey[Unit]("run-hello-assets"), Test, "fr.hsyl20.sme.tutorial.HelloAssets"),
    fullRunTask(TaskKey[Unit]("run-hello-loop"), Test, "fr.hsyl20.sme.tutorial.HelloLoop"),
    fullRunTask(TaskKey[Unit]("run-hello-input"), Test, "fr.hsyl20.sme.tutorial.HelloInput"),
    fullRunTask(TaskKey[Unit]("run-hello-material"), Test, "fr.hsyl20.sme.tutorial.HelloMaterial"),
    fullRunTask(TaskKey[Unit]("run-hello-animation"), Test, "fr.hsyl20.sme.tutorial.HelloAnimation"),
    fullRunTask(TaskKey[Unit]("run-hello-picking"), Test, "fr.hsyl20.sme.tutorial.HelloPicking"),
    fullRunTask(TaskKey[Unit]("run-hello-collision"), Test, "fr.hsyl20.sme.tutorial.HelloCollision"),
    fullRunTask(TaskKey[Unit]("run-hello-terrain"), Test, "fr.hsyl20.sme.tutorial.HelloTerrain"),
    fullRunTask(TaskKey[Unit]("run-hello-audio"), Test, "fr.hsyl20.sme.tutorial.HelloAudio"),
    fullRunTask(TaskKey[Unit]("run-hello-effects"), Test, "fr.hsyl20.sme.tutorial.HelloEffects"),
    fullRunTask(TaskKey[Unit]("run-hello-physics"), Test, "fr.hsyl20.sme.tutorial.HelloPhysics")
  )

  val myResolvers = Seq(
  )

  val myDependencies = Seq (
  )

  def nativeJavaLibraryPath: String = {

    val (os, separator) = System.getProperty("os.name").split(" ")(0).toLowerCase match {
      case "linux" => "linux" -> ":"
      case "mac" => "macosx" -> ":"
      case "windows" => "windows" -> ";"
      case "sunos" => "solaris" -> ":"
      case x => x -> ":"
    }

    val sep = java.io.File.separator
    
    System.getProperty("java.library.path") + separator + ("lib"+sep+"native"+sep+os)
  }
}

// Shell prompt which show the current project, 
// git branch and build version
object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  
  val current = """\*\s+([\w-]+)""".r
  
  def gitBranches = ("git branch --no-color" lines_! devnull mkString)
  
  val buildShellPrompt = { 
    (state: State) => {
      val currBranch = 
        current findFirstMatchIn gitBranches map (_ group(1)) getOrElse "-"
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, MyProject.buildVersion
      )
    }
  }
}

