package edu.luc.cs.laufer.cs371.expressions

import edu.luc.cs.laufer.cs371.expressions.ast.Expr
import edu.luc.cs.laufer.cs371.expressions.behaviors
//import edu.luc.cs.laufer.cs371.expressions.behaviors.Value
//import edu.luc.cs.laufer.cs371.expressions.behaviors.Store
import scala.collection.mutable.Map as MMap

object TestFixtures {

  import ast.Expr.*

//  val store: Store = MMap()

  val complex1 =
    Div(
      Minus(
        Plus(
          Constant(1),
          Constant(2)
        ),
        Times(
          Constant(3),
          Constant(4)
        )
      ),
      Constant(5)
    )

  val complex2 =
    Mod(
      Minus(
        Plus(
          Constant(1),
          Constant(2)
        ),
        Times(
          UMinus(
            Constant(3)
          ),
          Constant(4)
        )
      ),
      Constant(5)
    )

  val complex1string = "((1 + 2) - (3 * 4)) / 5"
  val complex2string2 = "((1+2) - ((-(3))*4)) % 5"
  val simplestring3 = "x = 2; y = 3; r = 0; while (y) { r = r + x ; y = y - 1; }"//Result: Num(0)
//  val sx = Assign("x", Constant(2))
//  val sy = Assign("y", Constant(3))
//  val sr = Assign("r", Constant(0))
//  val complexstring3 = Block(List[Expr](
//  Assign("x", Constant(2)),
//  Assign("y", Constant(3)),
//  Assign("r", Constant(0)),
//  Loop(Variable("y"),
//    Block(List[Expr](
//      Assign("r", Plus(Variable("r"), Variable("x"))),
//      Assign("y", Minus(Variable("y"), Constant(1)))
//    ))))
//)
  
  

/*  val complex3 =
    Mod(
      Minus(
        Plus(
          Constant(1),
          Constant(2)
        ),
        Times(
          UMinus(
            Constant(3)
          ),
          Constant(4)
        )
      ),
      Constant(5)
    )*/


//  val complex3: Expr =
//    Assign(//1
//      "complex3",
//      Block(//2
//        List(
//          Loop(//3
//            Constant(1),//4
//            Block(//4
//              List(
//                Assign("x", Constant(9)),//6
//                Variable("y"),//5
//                Cond(Constant(1), Assign("y", Constant(7))//7
//                  , Assign("z", Constant(9))//7
//                )
//              )
//            )
//          )
//        )
//      )
//    )
  //val complex4: Expr = Block(Cond(Plus(Plus(UMinus(Constant(3)),Constant(4)),Times(Constant(5),Constant(6))),Block(Loop(Constant(0),Block(Assign("x",Constant(3)),Assign(Variable("y"),Constant(5)),Block(List(Assign("xy",Constant(88)))))),Block(List[Expr]().empty))))
  //val complex4string4 = "{\n  if (((-3 + 4) + (5 * 6))) {\n    while (0) {\n      x = 3;\n      y = 5;\n      {\n        xy = 88;\n      }\n    }\n  } else {\n  }\n}"
  

  val complex4 =

    Mod(
      Minus(
        Plus(
          Constant(1),
          Constant(2)
        ),
        Times(
          UMinus(
            Constant(3)
          ),
          Constant(4)
        )
      ),
      Constant(5)
    )


  val complexString6 = "r = {}; r.course1 = {}; r.course1.firstExamScore = 25; r.course1.secondExamScore = 35; r.course1.totalScore = r.course1.firstExamScore + r.course1.secondExamScore; r.course2 = r.course1;"
  //r.course1.totalScore = r.course1.firstExamScore + r.course1.secondExamScore;
  val simple6 = Assign(
    Variable(
      "r"),
    Struct(Map[String, Expr]().empty
    ))
//  val course1 = Assign(
//  Select(Variable(
//    "r"),"course1"),
//  Struct(Map[String, Expr]().empty
//  ))
//  val firstExam = Assign(Se
//  Variable(
//    "r"), "course1.firstExamScore,
//  25)
  val complexstring7 = "l = { head: 1, tail: { head: 2, tail: { head: 3, tail: 0 } } };"
//  val complexstring7res = Assign(
//  Variable(
//    "l"),
//  Struct(Map[String, Expr](
//  "head" -> Constant(1),
//  "tail" -> Struct(Map[String, Expr]
//      "head"-> Constant(2),
//      "tail" -> Struct(
//      "head" -> Constant(3),
//      "tail" -> Constant(0))
//      )
//    )
//  )
//  )
val simple7 = "r = {};"
val simple7unPar = Assign(
  Variable(
    "r"),
  Struct(Map[String,Expr]().empty
  ))

  val complex5 =
    Mod(
      Minus(
        Plus(
          Constant(1),
          Constant(2)
        ),
        Times(
          UMinus(
            Constant(3)
          ),
          Constant(4)
        )
      ),
      Constant(5)
    )






  

}
