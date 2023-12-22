package edu.luc.cs.laufer.cs371.expressions

import edu.luc.cs.laufer.cs371.expressions.TestFixtures.*
import edu.luc.cs.laufer.cs371.expressions.ast.Expr
import edu.luc.cs.laufer.cs371.expressions.behaviors
import edu.luc.cs.laufer.cs371.expressions.behaviors.*
import org.scalatest.funsuite.AnyFunSuite
import CombinatorCalculator.store

import scala.util.{Failure, Success, Try}

object Main {
  def main(args: Array[String]): Unit = {
    println("p = " + complex1)
    //println("evaluate(p) = " + evaluate(complex1))
    println("size(p) = " + size(complex1))
    println("height(p) = " + height(complex1))
    println(toFormattedString(complex1))

    println("q = " + complex2)
    //println("evaluate(q) = " + evaluate(complex2))
    println("size(q) = " + size(complex2))
    println("height(q) = " + height(complex2))
    println(toFormattedString(complex2))

//    println("c3 = " + complex3)
//    println("size(c3) = " + size(complex3))
//    println("height(c3) = " + height(complex3))
//    println(toFormattedString(complex3))

//    println("complexstring3 = " + complexstring3)
//    println("size(cs3) = " + size(complexstring3))
//    println("height(cs3)" + height(complexstring3))
//    println("evaluate(cs3) = " + behaviors.evaluate(store)(complexstring3))
      //println("evaluate(cs7):" + evaluate(store)(complexstring7))
  }
}
//
class Test extends AnyFunSuite {
  //test("evaluate(p)") { assert(evaluate(complex1) == -1) }
  test("size(p)") { assert(size(complex1) == 9) }
  test("height(p)") { assert(height(complex1) == 4) }
  //test("evaluate(q)") { assert(evaluate(complex2) == 0) }
  test("size(q)") { assert(size(complex2) == 10) }
  test("height(q)") { assert(height(complex2) == 5) }
  //Complex 3
//  test("size(c3)"){assert(size(complex3) == 14)}
//  test("height(c3)"){assert(height(complex3) == 7)}

  //val complexstring3evaluate = behaviors.evaluate(store)(complexstring3)
//  test("evaluate(cs3)"){assert(behaviors.evaluate(store)(complexstring3) === Success(Num(0)))}
//  test("store"){assert(store === Map("y" -> Num(0), "x" -> Num(2), "r" -> Num(6)))}
//test("store"){store == Map(l -> Ins(HashMap(head -> Num(1), tail -> Ins(HashMap(head -> Num(2), tail -> Ins(HashMap(head -> Num(3), tail -> Num(0))))))), "curr" -> Ins(Map("head" -> Num(1), "tail" -> Ins(Map("head" -> Num(2), "tail" -> Ins(Map("head" -> Num(3), "tail" -> Num(0))))))))}
}
