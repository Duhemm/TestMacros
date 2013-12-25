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
      val params = T.members.sorted.collect {
        case x: MethodSymbol if x.isCaseAccessor =>
          val tpe = x.returnType
          q"if(cc.${x.name} == null) null else implicitly[scala.reflect.api.Liftable[$tpe]].apply(universe, cc.${x.name})"
      }

      q"""
      implicit object Foo extends scala.reflect.api.Liftable[$T] {
        def apply(universe: scala.reflect.api.Universe, cc: $T): universe.Tree = {
          val ttree = universe.Ident(universe.TermName(${T.typeSymbol.name.decoded}))
      
          universe.Apply(universe.Select(universe.New(ttree), universe.nme.CONSTRUCTOR), List(..$params))
        }
      }
      Foo
    """
    }
  }
}