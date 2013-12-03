import scala.reflect.macros.WhiteboxContext
import scala.language.experimental.macros
import scala.reflect.api.Liftable
import scala.reflect.macros.Universe
import scala.reflect.api.Position

object Macros {

  implicit def liftableCaseClass[T]: Liftable[T] = macro impl[T]

  def impl[T: c.WeakTypeTag](c: WhiteboxContext) = {
    import c.universe._

    val T = weakTypeOf[T]
    if (!T.typeSymbol.asClass.isCaseClass) c.abort(c.enclosingPosition, "Not a case class")
    else {
      val params = T.members.collect { case x: MethodSymbol if x.isCaseAccessor => q"cc.$x" }.toList.reverse
      q"new Liftable[$T] { def apply(universe: reflect.api.Universe, cc: $T) = new $T(..$params) }"
    }
  }

}