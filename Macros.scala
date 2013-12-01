import scala.reflect.macros.BlackboxContext
import scala.language.experimental.macros
import scala.reflect.api.Liftable
import scala.reflect.macros.Universe
import scala.reflect.api.Position
import scala.reflect.runtime.currentMirror

class LiftableCaseClass[T] extends Liftable[T with Product] {
  def apply(universe: reflect.api.Universe, value: T with Product): universe.Tree = {
    import universe._

    val T = weakTypeOf[T]
    
    val params = value.productIterator.map {
      case x if Macros.isCaseClass(x) => apply(universe, x.asInstanceOf[T with Product])
      case x => Literal(Constant(x))
    }.toList
    
    val name = TypeName(value.getClass.getName)

    q"$name(..$params)"
  }
}

object Macros {

  def isCaseClass(t: Any): Boolean = currentMirror.reflect(t).symbol.isCaseClass

  implicit def liftableCaseClass[T]: Liftable[T] = macro impl[T]

  def impl[T: c.WeakTypeTag](c: BlackboxContext) = {
    import c.universe._
    val T = weakTypeOf[T]
    
    q"new LiftableCaseClass[$T]()"
  }

}