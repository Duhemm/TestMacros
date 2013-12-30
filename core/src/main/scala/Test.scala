case class A(x: Int)
case class B(x: Int, y: String)
case class C(x: Boolean)
case class D(x: A, y: B, z: C)
case class E(x: E)
case class F(g: G)
case class G(f: F)

object Test extends App {

  import Macros._
  import reflect.api.Liftable
  import scala.reflect.runtime.universe._
  import scala.reflect.runtime.currentMirror

  val c1 = A(18)
  val c2 = B(1, "Second value")
  val c3 = C(true)
  val c4 = D(c1, c2, c3)
  val c5 = E(E(E(E(E(E(E(null)))))))
  val c6 = F(G(null))
  val c7 = G(null)

  val r1 = q"$c1"
  val r2 = q"$c2"
  val r3 = q"$c3"
  val r4 = q"$c4"
  val r5 = q"$c5"
  val r6 = q"$c6"
  val r7 = q"$c7"

  println("RAW : " + showRaw(r1))
  println("RAW : " + showRaw(r2))
  println("RAW : " + showRaw(r3))
  println("RAW : " + showRaw(r4))
  println("RAW : " + showRaw(r5))
  println("RAW : " + showRaw(r6))
  println("RAW : " + showRaw(r7))

  println(r1)
  println(r2)
  println(r3)
  println(r4)
  println(r5)
  println(r6)
  println(r7)

}