package edu.luc.cs.laufer.cs371.expressions

import scala.util.parsing.combinator.JavaTokenParsers
import ast.Expr
import Expr.*

import scala.collection.View.Empty
import scala.language.postfixOps


object CombinatorParser extends JavaTokenParsers {

  /**
   * Enable missing typesafe equality between `None` and `Option`.
   * TODO remove once the library provides this.
   */
  given CanEqual[None.type, Option[_]] = CanEqual.derived

  /** expr ::= term { { "+" | "-" } term }* */
  def expr: Parser[Expr] =
    term ~! rep(("+" | "-") ~ term) ^^ {
      case l ~ es => es.foldLeft(l)({
        case (res, "+" ~ e) => Plus(res, e)
        case (res, "-" ~ e) => Minus(res, e)
      })
    }

  /** term ::= factor { { "*" | "/" | "%" } factor }* */
  def term: Parser[Expr] =
    factor ~! rep(("*" | "/" | "%") ~ factor) ^^ {
      case l ~ es => es.foldLeft(l)({
        case (res, "*" ~ e) => Times(res, e)
        case (res, "/" ~ e) => Div(res, e)
        case (res, "%" ~ e) => Mod(res, e)
      })
    }

  def selection: Parser[Expr] =
    ident ~! rep("." ~ ident) ^^ {
      case l ~ es => es.foldLeft[Expr](Variable(l)) {
        case (res, "." ~ e) => Select(res, e)
      }
    }

  /** factor ::= wholeNumber | "+" factor | "-" factor | "(" expr ")" */
  //3c: factor ::= ident { "." ident }* | number | "+" factor | "-" factor | "(" expr ")" | struct
  def factor: Parser[Expr] = (
    selection ^^ { case s => s }
      | wholeNumber ^^ { case s => Constant(s.toInt) }
      | ident ^^ { case s => Variable(s) }
      | "+" ~> factor ^^ { case e => e }
      | "-" ~> factor ^^ { case e => UMinus(e) }
      | "(" ~ expr ~ ")" ^^ { case _ ~ e ~ _ => e }
      | struct ^^ { case e => e }
    )

  //statement   ::= expression ";" | assignment | conditional | loop | block
  def statement: Parser[Expr] = {
    expr ~ ";" ^^ { case e ~ _ => e }
      | assignment ^^ { case a => a }
      | conditional ^^ { case c => c }
      | loop ^^ { case l => l }
      | block ^^ { case b => b }
      | struct ^^ { case e => e }
  }

  //assignment  ::= ident "=" expression ";"
  //UPDATE for 3c: assignment  ::= ident { "." ident }* "=" expression ";"
  def assignment: Parser[Expr] =
    selection ~ "=" ~ expr ~ ";" ^^ {
      case l ~ "=" ~ r ~ ";" => Assign(l, r)
    }

  //conditional ::= "if" "(" expression ")" block [ "else" block ]
  def conditional: Parser[Expr] = {
    "if" ~ "(" ~ expr ~ ")" ~ block ~! opt("else" ~ block) ^^ {
      case "if" ~ "(" ~ guard ~ ")" ~ thenBranch ~ None => Cond(guard, thenBranch, Block(List[Expr]().empty))
      case "if" ~ "(" ~ guard ~ ")" ~ thenBranch ~ Some("else", g) => Cond(guard, thenBranch, g)
    }
  }

  //loop  ::= "while" "(" expression ")" block
  def loop: Parser[Expr] = {
    "while" ~ "(" ~ expr ~ ")" ~ block ^^ {
      case _ ~ _ ~ g ~ _ ~ b => Loop(g, b)
    }
  }

  //block       ::= "{" statement* "}"
  def block: Parser[Expr] = {
    "{" ~ (statement *) ~ "}" ^^ {
      case "{" ~ statement ~ "}" => Block(statement)
    }
  }

  def statements: Parser[Expr] = {
    rep(statement) ^^ { case ss => Sequence(ss: _*) }
  }

  //field  ::= ident ":" expr
  def field: Parser[Expr] = {
    ident ~ ":" ~ expr ^^ { case l ~ _ ~ r => Field(l, r) }
      | ident ~ ":" ~ struct ^^ { case l ~ _ ~ r => Field(l, r) }
  }

  //struct ::= "{" "}" | "{" field { "," field }* "}"
  def struct: Parser[Expr] = {
    "{" ~ "}" ^^ {case "{" ~ "}" => Struct(Map[String, Expr]().empty)}
      | "{" ~> field ~ ({"," ~> field}*) <~ "}" ^^ {case lf ~ rf  =>
      Struct(rf.foldLeft{
        Map[String, Expr](lf.asInstanceOf[Field].left -> lf.asInstanceOf[Field].right)
      }{
        case (map, next) => map ++ Map[String, Expr](next.asInstanceOf[Field].left -> next.asInstanceOf[Field].right)
      }
      )
    }
  }
}
