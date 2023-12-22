package edu.luc.cs.laufer.cs371.expressions
import edu.luc.cs.laufer.cs371.expressions.behaviors.*
import scala.collection.mutable.Map as MMap

object CombinatorCalculator {
  type Store = MMap[String, Value]
  val store: Store = MMap.empty
  
  def processExpr(input: String): Unit = {
    println("You entered: " + input)
    val result = CombinatorParser.parseAll(CombinatorParser.statements, input)
    if result.isEmpty then {
      println("This expression could not be parsed")
    } else {
      import behaviors.*
      val expr = result.get
      println("The parsed expression is: ")
      println(toFormattedString(expr))
      println("The unparsed expression is: ")
      println(toUnparser(expr))
//      println("It has size " + size(expr) + " and height " + height(expr))
      println("It evaluates to " + evaluate(store)(expr))
      println("Memory: " + store)
    }
  }

  def main(args: Array[String]): Unit = {
    if args.length > 0 then {
      processExpr(args mkString " ")
    } else {
      print("Enter infix expression: ")
      scala.io.Source.stdin.getLines() foreach { line =>
        processExpr(line)
        print("Enter infix expression: ")
      }
    }
  }
}
