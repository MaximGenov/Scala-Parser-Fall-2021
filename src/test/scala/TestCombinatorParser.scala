package edu.luc.cs.laufer.cs371.expressions

import org.scalatest.funsuite.AnyFunSuite
import TestFixtures.*
import edu.luc.cs.laufer.cs371.expressions.ast.Expr

object MainCombinatorParser {
  def main(args: Array[String]): Unit = {
    val parsedExpr = CombinatorParser.parseAll(CombinatorParser.expr, complex1string)
    println(parsedExpr.get)
    println(complex1)
    println(parsedExpr.get == complex1)
//    println(behaviors.evaluate(parsedExpr.get))
  }
}

class TestCombinatorParser extends AnyFunSuite {
  val parsedExpr = CombinatorParser.parseAll(CombinatorParser.expr, complex1string)

//  val parsedExpr2 = CombinatorParser.parseAll(CombinatorParser.expr, complex1string2)
//  val parsedExpr4 = CombinatorParser.parseAll(CombinatorParser.statement, complex4string4)

  val parsedExpr2 = CombinatorParser.parseAll(CombinatorParser.expr, complex2string2)
  
//  val parsedExpr3 = CombinatorParser.parseAll(CombinatorParser.expr, complex3string3)
//  val parsedExpr4 = CombinatorParser.parseAll(CombinatorParser.expr, complex4string4)
//  val parsedExpr5 = CombinatorParser.parseAll(CombinatorParser.expr, complex5string5)
  val parsedExpr6 = CombinatorParser.parseAll(CombinatorParser.expr, simplestring3)
  //val parsedExpr7 = CombinatorParser.parseAll(CombinatorParser.expr, complexString6)
  //val parsedexpr8 = CombinatorParser.parseAll((CombinatorParser.expr, complexstring7))
  val parsedSimple = CombinatorParser.parseAll(CombinatorParser.expr, simple7)

  test("parser works 1") { assert(parsedExpr.get == complex1) }
  test("parser works 2") { assert(parsedExpr2.get == complex2) }
//  test("parser works 3") { assert(parsedExpr3.get == complex3) }
//  test("parser works 4") { assert(parsedExpr4.get == complex4) }
//  test("parser works 5") { assert(parsedExpr5.get == complex5) }
  //test("parser works 3"){assert(parsedExpr6.get == )}
  //test("parser works 7"){ assert(parsedExpr7.get == simple6)}
  //test("parser works 8"){assert(parsedexpr8.get == complexstring7res)}
  //test("simple parser works"){assert(parsedSimple.get == simple7unPar)}


}
