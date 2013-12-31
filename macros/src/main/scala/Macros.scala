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
    else if (c.enclosingImplicits.tail.exists(_.pt == c.enclosingImplicits.head.pt)) c.abort(c.enclosingPosition, "workaround")
    else {
      val params = T.members.sorted.collect {
        case x: MethodSymbol if x.isCaseAccessor =>
          val tpe = x.returnType
          val clssSymbol = tpe.typeSymbol.asClass
          val liftMember = q"scala.Predef.implicitly[scala.reflect.api.Liftable[$tpe]].apply(universe, cc.${x.name})"

          if (clssSymbol.isPrimitive || clssSymbol.isDerivedValueClass) {
            liftMember
          } else {
            q"if(cc.${x.name} == null) null else $liftMember"
          }
      }

      val objName = c.universe.TermName(c.freshName("LiftableFor" + T))

      q"""
      implicit object $objName extends scala.reflect.api.Liftable[$T] {
        def apply(universe: scala.reflect.api.Universe, cc: $T): universe.Tree = {
          val ttree = universe.Ident(universe.TermName(${T.typeSymbol.name.encoded}))
      
          universe.Apply(universe.Select(universe.New(ttree), universe.nme.CONSTRUCTOR), scala.collection.immutable.List(..$params))
        }
      }
      $objName
      """
    }
  }
}