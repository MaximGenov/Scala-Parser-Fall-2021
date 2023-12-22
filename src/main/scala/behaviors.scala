package edu.luc.cs.laufer.cs371.expressions

import ast.Expr, Expr.*
import scala.collection.mutable.Map as MMap
import scala.util.{Failure, Success, Try}

object behaviors {
  trait Value
    case class Num(value: Int) extends Value
    case class Ins(struct: Store) extends Value

  type Result = Try[Value]
  type Store = MMap[String, Value]

  def evaluate(store: Store)(e: Expr): Result = e match {
    case Constant(c) => Success(Num(c))
    case Variable(n) => lookup(store)(n)//Try(store(n))//lookup(store)(n)

    case UMinus(r)   =>
      for
        Num(r) <- evaluate(store)(r)
      yield Num(-r)

    case Plus(l, r)  =>
      for
        Num(resl) <- evaluate(store)(l)
        Num(resr) <- evaluate(store)(r)
      yield Num(resl + resr)

    case Minus(l, r) =>
      for
        Num(resl) <- evaluate(store)(l)
        Num(resr) <- evaluate(store)(r)
      yield Num(resl - resr)

    case Times(l, r) =>
      for
        Num(resl) <- evaluate(store)(l)
        Num(resr) <- evaluate(store)(r)
      yield Num(resl * resr)

    case Div(l, r)   =>
      for
        Num(resl) <- evaluate(store)(l)
        Num(resr) <- evaluate(store)(r)
      yield Num(resl / resr)

    case Mod(l, r)   =>
      for
        Num(resl) <- evaluate(store)(l)
        Num(resr) <- evaluate(store)(r)
      yield Num(resl % resr)

    case Assign(l, r) =>
      val rvalue = evaluate(store)(r)
      val lvalue = evaluate(store)(l)
      lvalue match {
        case f @ Failure(n) => //store.put(l.toString, rvalue.get)
          l match {
            case Select(r, f) => store.put(l.asInstanceOf[Select].field, rvalue.get)
            case Variable(s) => store.put(l.asInstanceOf[Variable].name, rvalue.get)
          }
        case Success(n) =>  //store.put(l.toString, rvalue.get)
          l match {
            case Select(r, f) => store.put(l.asInstanceOf[Select].field, rvalue.get)
            case Variable(s) => store.put(l.asInstanceOf[Variable].name, rvalue.get)
          }
      }
      Try(Num(0))
    //      evaluate(store)(r) match {
    //        case Success(v) =>
    //          store.update(l, v)
    //          Success(Num(0))
    //        case f @ Failure(_) => f
    //      }

    case Select(rec, f) =>
      evaluate(store)(rec)
      evaluate(store)(Variable(f))

    case Cond(g, t, e) =>
      evaluate(store)(g) match {
        case Success(Num(0)) => evaluate(store)(e)
        case Success(_) => evaluate(store)(t)
        case f @ Failure(_) => f
      }

    case Sequence(statements @ _*) =>
      statements.foldLeft(Try { Num(0)}:Result)((c: Result, s) => evaluate(store)(s)) //TODO CleanUp

    case Loop(g, b) =>
      var gValue = for
        evg <- evaluate(store)(g)
      yield evg
      while gValue.get.asInstanceOf[Num].value != 0 do
        evaluate(store)(b)
        gValue = evaluate(store)(g)
      Success(Num(0))

    case Block(e) =>
      for ex <- e do
        for
          exp <- evaluate(store)(ex)
        yield exp
      Success(Num(0))

    case Struct(m) =>
      val ins = scala.collection.mutable.Map[String, Value]()
      for (k, v) <- m do {
        val res = evaluate(store)(v);
        if res.isFailure then return res;
        ins(k) = res.get
      }
      Success(Ins(ins))

  }

  def lookup(store: Store)(n: String): Result =
    store.get(n).fold{
      Failure(new NoSuchFieldException(n)): Result
    } {
      Success(_)
    }


  def size(e: Expr): Int = e match {
    case Constant(c) => 1
    case UMinus(r)   => 1 + size(r)
    case Plus(l, r)  => 1 + size(l) + size(r)
    case Minus(l, r) => 1 + size(l) + size(r)
    case Times(l, r) => 1 + size(l) + size(r)
    case Div(l, r)   => 1 + size(l) + size(r)
    case Mod(l, r)   => 1 + size(l) + size(r)
    case s: Sequence => s.exprs.foldLeft(1)((res,e)=>res+size(e)) //TODO CleanUp
    case Variable(name) => 1
    case Assign(l, r) => 2 + size(r)
    case b: Block => b.expressions.map(size).sum
    case Cond(bool, thn, els) => 1 + size(els) + size(thn)
    case Loop(guard, body) => 1 + size(body)
  }

  def height(e: Expr): Int = e match {
    case Constant(c) => 1
    case UMinus(r)   => 1 + height(r)
    case Plus(l, r)  => 1 + math.max(height(l), height(r))
    case Minus(l, r) => 1 + math.max(height(l), height(r))
    case Times(l, r) => 1 + math.max(height(l), height(r))
    case Div(l, r)   => 1 + math.max(height(l), height(r))
    case Mod(l, r)   => 1 + math.max(height(l), height(r))
    case Variable(name) => 1
    case Assign(l, r) => 2+size(r)//1 + math.max(height(r), height(l))
    case s: Sequence => s.exprs.foldLeft(1)((res,e)=>math.max(res,height(e))) //TODO CleanUp
    case b: Block => 1 + b.expressions.map(height).max
    case Cond(bool, thn, els) => {
      if !els.equals(Block(List[Expr]().empty)) then
        1 + Seq(height(bool), height(thn), height(els)).max
      else 1 + Seq(height(bool), height(thn)).max
    }
    case Loop(guard, body) => 1 + math.max(height(guard), height(body))
  }

  def toFormattedString(prefix: String)(e: Expr): String = e match {
    case Constant(c) => prefix + c.toString
    case UMinus(r)   => buildUnaryExprString(prefix, "UMinus", toFormattedString(prefix + INDENT)(r))
    case Plus(l, r)  => buildExprString(prefix, "Plus", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Minus(l, r) => buildExprString(prefix, "Minus", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Times(l, r) => buildExprString(prefix, "Times", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Div(l, r)   => buildExprString(prefix, "Div", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Mod(l, r)   => buildExprString(prefix, "Mod", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Variable(name) => buildUnaryExprString(prefix, "Variable", name)
    case Assign(l, r) => buildExprString(prefix, "Assign", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case b: Block => buildUnaryExprString(prefix, "Block", b.expressions.map(l => toFormattedString(prefix + INDENT)(l)).mkString(INDENT))
    case et: Sequence => et.exprs.asInstanceOf[List[Expr]].map(x => toFormattedString(prefix + INDENT)(x)).mkString(INDENT) //TODO CleanUp
    case Cond(bool, thn, els) => buildExprString(prefix, "Cond", toFormattedString(prefix + INDENT)(thn), toFormattedString(prefix + INDENT)(els))
    case Loop(guard, body) => buildExprString(prefix, "Loop", toFormattedString(prefix + INDENT)(guard), toFormattedString(prefix + INDENT)(body))
    case Select(e, f) => toFormattedString(prefix)(e) + "." + f
    case Struct(f)=> buildUnaryExprString(prefix, "Struct", f.map((field) => prefix + INDENT + field._1 + ":" + EOL + toFormattedString(prefix + INDENT)(field._2)).mkString("," + EOL))
  }

  def toFormattedString(e: Expr): String = toFormattedString("")(e)

  def buildExprString(prefix: String, nodeString: String, leftString: String, rightString: String) = {
    val result = new StringBuilder(prefix)
    result.append(nodeString)
    result.append("(")
    result.append(EOL)
    result.append(leftString)
    result.append(", ")
    result.append(EOL)
    result.append(rightString)
    result.append(")")
    result.toString
  }

  def buildUnaryExprString(prefix: String, nodeString: String, exprString: String) = {
    val result = new StringBuilder(prefix)
    result.append(nodeString)
    result.append("(")
    result.append(EOL)
    result.append(exprString)
    result.append(")")
    result.toString
  }

  val EOL = scala.util.Properties.lineSeparator
  val INDENT = ".."

  def toUnparser(prefix: String)(e: Expr): String = e match {
    case Variable(a) => prefix + a
    case Constant(b) => prefix + b.toString
    case UMinus(c) => prefix + toUnparser(prefix)(c)
    case Plus(d, e) => prefix + toUnparser(prefix)(d) + " +" + toUnparser(prefix)(e)
    case Minus(f, g) => prefix + toUnparser(prefix)(f) + " -" + toUnparser(prefix)(g)
    case Times(h, i) => prefix + toUnparser(prefix)(h) + " *" + toUnparser(prefix)(i)
    case Div(j, k) => prefix + toUnparser(prefix)(j) + " /" + toUnparser(prefix)(k)
    case Mod(l, m) => prefix + toUnparser(prefix)(l) + " %" + toUnparser(prefix)(m)
    case Loop(n, o) => prefix + "while( " + toUnparser(prefix)(n) + ") { " + EOL + toUnparser(prefix)(o) + "}" + EOL
    case Assign(q, r) => prefix + INDENT + q + " = " + toUnparser(prefix)(r) + " ;"
    case Cond(s, t, u) => {(
      prefix + "{" + EOL + INDENT + "if" + "(" + toUnparser(s) + ")" + "{" + EOL + INDENT + INDENT + toUnparser(t) + EOL + INDENT + "}" + EOL
      + INDENT + "else" + "{" + EOL + INDENT + INDENT + toUnparser(u) + EOL + INDENT + "}" + EOL + "}" )}
    case b: Block => b.expressions.map(x => toUnparser(x)).mkString(EOL)
    case s: Sequence => s.exprs.map(x=>toUnparser(x)).mkString(EOL) //TODO CleanUp
    case Select(e, f) => toUnparser(prefix)(e) + "." + f
    case Struct(m) => prefix + "{" + EOL + m.map((f) => prefix + INDENT + f._1 + ":" + toUnparser(f._2)).mkString(", " + EOL) + EOL + prefix + "}"

  }

  def toUnparser(e: Expr): String = toUnparser("")(e)
}
