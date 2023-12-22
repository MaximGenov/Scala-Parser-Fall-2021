package edu.luc.cs.laufer.cs371.expressions.ast

/** An initial algebra of arithmetic expressions. */
enum Expr derives CanEqual:
  case Constant(value: Int)
  case UMinus(expr: Expr)
  case Plus(left: Expr, right: Expr)
  case Minus(left: Expr, right: Expr)
  case Times(left: Expr, right: Expr)
  case Div(left: Expr, right: Expr)
  case Mod(left: Expr, right: Expr)
  case Variable(name: String)
  case Block(expressions: List[Expr])
  case Cond(guard: Expr, thenBranch: Expr, elseBranch: Expr)
  case Loop(guard: Expr, body: Expr)
  
  case Assign(left: Expr, right: Expr)
  
  case Sequence(exprs: Expr*)
  
  case Struct(map: Map[String, Expr])
  case Field(left: String, right: Expr)
  case Select(reciever: Expr, field: String)