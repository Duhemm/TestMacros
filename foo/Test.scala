package foo

case class A(x: Int)
case class B(x: Int, y: String)
case class C(x: List[Int])
case class D(x: A, y: B, z: C)

object Test extends App {

  import foox.Macros._
  //import reflect.runtime.universe._
  import reflect.api.Liftable
  import scala.reflect.runtime.universe._
  import scala.reflect.runtime.currentMirror
  
  /*implicit object liftA extends Liftable[A] {
    def apply(universe: reflect.api.Universe, cc: A): universe.Tree = {

      val ttree = universe.Ident(universe.TermName("A"))

      val t = universe.Ident(universe.TermName("A")) //universe.TypeTree(universe.typeOf[A])
      
      println("TYPE : " + showRaw(t))
      
      /*universe.Apply(
        universe.Apply(
          universe.Select(universe.New(universe.TypeTree(universe.typeOf[A])), universe.nme.CONSTRUCTOR),
          List(universe.Literal(universe.Constant(2)))),
        List(universe.Literal(universe.Constant(3))))*/
      universe.Apply(universe.Select(universe.New(ttree), universe.nme.CONSTRUCTOR), List(universe.Literal(universe.Constant(cc.x))))

    }
  }
*/

  val c1 = A(18)
  /* val c2 = B(1, "Second value")
  val c3 = C(List(1, 2, 3))
  val c4 = D(c1, c2, c3)*/

  val r1 = q"$c1"
  /*val r2 = q"$c2"
  val r3 = q"$c3"
  val r4 = q"$c4"*/
  println("RAW : " + showRaw(r1))

  println(r1)
  /*println(r2)
  println(r3)
  println(r4)*/
}