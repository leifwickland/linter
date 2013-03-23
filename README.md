# Linter Compiler Plugin

This is a compiler plugin that adds additional lint checks to protect against sharp corners
in the Scala compiler and standard libraries.

It's a work in progress.  For an overview of writing compiler plugins, see http://www.scala-lang.org/node/140

## Usage

Add it as a compiler plugin in your project by editing your build.sbt file.  For example, once published:

    addCompilerPlugin("com.foursquare.lint" %% "linter" % "x.y.z")

Or, until published:

    scalacOptions += "-Xplugin:..path-to-jar../linter.jar"

Optionally, run `sbt console` in this project to see it in action.

## Currently suported warnings

### Using `scala.io.Source.fromFile` without closing file
    scala> io.Source.fromFile("README.md").mkString
    <console>:8: warning: You should close the file stream after use.
                  io.Source.fromFile("README.md").mkString
                                                   ^
                                                   
### Implicit methods without an explicit return type
    scala> implicit def int2string(a:Int) = a.toString
    <console>:8: warning: Implicit method int2string needs explicit return type
           implicit def int2string(a:Int) = a.toString
                        ^

### Unsafe `==`
    scala> Nil == None
    <console>:29: warning: Comparing with == on instances of different types (object Nil, object None) will probably return false.
                  Nil == None
                      ^
    scala> val a = 0.1; a == 0.4
    <console>:9: warning: Exact comparison of floating point values is potentially unsafe.
                  a == 0.4
                    ^

### Unsafe `contains`
    scala> List(1, 2, 3).contains("4")
    <console>:29: warning: List[Int].contains(String) will probably return false.
                  List(1, 2, 3).contains("4")
                                ^

### Wildcard import from `scala.collection.JavaConversions`
    scala> import scala.collection.JavaConversions._
    <console>:29: warning: Conversions in scala.collection.JavaConversions._ are dangerous.
           import scala.collection.JavaConversions._
                                   ^

### Any and all wildcard imports
    scala> import scala.collection.mutable._
    <console>:7: warning: Wildcard imports should be avoided. Favor import selector clauses.
           import scala.collection.mutable._
                                   ^

### Calling `Option#get`
    scala> Option(1).get
    <console>:29: warning: Calling .get on Option will throw an exception if the Option is None.
                  Option(1).get
                            ^
                            
### Literal division by zero
    scala> 100 / (1+1 - 2)
    <console>:8: warning: Literal division by zero.
                  100 / (1+1 - 2)
                      ^

### Unnecessary if statement
    scala> val a,b = 5; if(a == b && b > 5) true else false
    <console>:9: warning: Remove the if and just use the condition: this.a.==(this.b).&&(this.b.>(5))
            if(a == b && b > 5) true else false
            ^

## Future Work

* Add more warnings
* Pick and choose which warnings you want
* Choose whether they should be warnings or errors

### Ideas for new warnings

Feel free to implement these, or add your own ideas. Pull requests welcome!

* Modularize the wildcard import warnings like the "Avoid Star Import" configuration of checkstyle
 (http://checkstyle.sourceforge.net/config_imports.html)
* Require explicit `override` whenever a method is being overwritten
* Expressions spanning multiple lines should be enclosed in parentheses
* Unused method argument warnings
* Warn on unrestricted catch clauses (`case e => ...`)
* Traversable#head, Traversable#last, Traversable#maxBy
* Warn on shadowing variables, especially those of the same type
* Warn on inexhaustive pattern matching
* Boolean function parameters should be named (`func("arg1", force = true)`)
