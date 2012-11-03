# Demo
* http://desolate-wildwood-9229.herokuapp.com/ with credentials docear freeplane537

# Installation 
1. make sure you have installed a JDK (6 or 7) correctly
    * open a console and enter `javac -version`, that should print your java version, for example `javac 1.6.0_37`
    * you may have to add your Java *bin* folder to the PATH environment variable and open a new console window
1. checkout the project
1. go to the project folder with the console
    * check: `dir` (or `ls` on UNIX) should show you the folders app, conf, project and some more
1. then enter the command `run` or `run.bat`, this may take a while the first time because it downloads a lot of JARs, you may even think that it hangs
1. you can view the application in the browser after it printed something like `play - Listening for HTTP on port 9000...`
1. open [http://localhost:9000](http://localhost:9000) in your browser
1. use Ctrl + D or Ctrl + C to stop the application

# Programming
* Play Documentation: http://www.playframework.org/documentation/2.0.4/Home
    * Java Developing: http://www.playframework.org/documentation/2.0.4/JavaHome
    * Java API: http://www.playframework.org/documentation/api/2.0.4/java/index.html
* Generate IDE Project Files
      * `sbt eclipsify` #Eclipse
      * `sbt idea` #IntelliJ IDEA
      * Netbeans: http://www.playframework.org/documentation/2.0.4/IDE
* run unit tests in batch mode: `sbt test`
* run QUnit tests in Browser [http://localhost:9000/qunit](http://localhost:9000/qunit)
* Writing QUnit tests:
     * currently: add a coffee file in app/assets/javascripts/test and add it to app/views/qunit/test.scala.html as JavaScript link

# Deployment
## Heroku
1. get invitation for collaboration from Michael Schleichardt with your e-mail adress
1. install the heroku toolbelt: https://toolbelt.herokuapp.com/
1. checkout the project from github
1. git remote add heroku git@heroku.com:desolate-wildwood-9229.git
1. git push heroku # deploys into the cloud, may take a while

## Tomcat
1. `sbt war`
1. rename the war file to ROOT.war
1. put ROOT.war in tomcat/webapps
1. start Tomcat


# Links
## CoffeeScript
* http://autotelicum.github.com/Smooth-CoffeeScript/interactive/interactive-coffeescript.html