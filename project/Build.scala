import sbt._
import Keys._

import play.Project._

object ApplicationBuild extends Build {

    val appName         = "w3act"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      javaCore,
      javaJdbc,
      javaEbean
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
        // Add your own project settings here      
	    testOptions in Test ~= { args =>
	      for {
	        arg <- args
	        val ta: Tests.Argument = arg.asInstanceOf[Tests.Argument]
	        val newArg = if(ta.framework == Some(TestFrameworks.JUnit)) ta.copy(args = List.empty[String]) else ta
	      } yield newArg
	    }    
    )    

}
            
