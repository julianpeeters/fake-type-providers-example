package fake_type_providers

import language.experimental.macros
import scala.reflect.macros.Context
import scala.io._

trait Schema

object DynamicSchemaMaker extends ReflectionUtils with SchemaParsingUtils {
  import scala.language.dynamics

  class DynamicSchema[P <: String] extends Schema with Dynamic {
    def selectDynamic(name: String) = macro DynamicSchemaMaker.selectDynamic[P]
  }

  val schemas = scala.collection.mutable.Map.empty[String, Map[String, String]]

  def apply(path: String) = macro apply_impl

  def apply_impl(c: Context)(path: c.Expr[String]): c.Expr[Schema] = {
    import c.universe._

    val pathLit = path.tree match {
      case Literal(Constant(pathLit: String)) => pathLit
      case _ => c.abort(
        c.enclosingPosition,
        "You must provide a literal resource path for schema parsing!"
      )
    }
    
    val stream = Option(this.getClass.getResourceAsStream(pathLit)).getOrElse(
      c.abort(c.enclosingPosition, "Invalid resource path!")
    )

    this.schemas(pathLit) = Source.fromInputStream(stream).getLines.map(
      parseLine(_).fold(c.abort(c.enclosingPosition, _), identity)
    ).toMap

    c.Expr(
      Apply(
        Select(
          New(
            TypeTree(
              appliedType(
                typeOf[DynamicSchema[_]].typeConstructor,
                ConstantType(Constant(pathLit)) :: Nil
              )
            )
          ),
          nme.CONSTRUCTOR
        ),
        Nil
      )
    )
  }

  def selectDynamic[P <: String: c.WeakTypeTag](c: Context)(name: c.Expr[String]) = {
    import c.universe._

    val schema = weakTypeOf[P] match {
      case ConstantType(Constant(path: String)) => schemas.getOrElse(
        path,
        c.abort(c.enclosingPosition, "This schema hasn't been parsed!")
      )
      case _ => c.abort(c.enclosingPosition, "Something really bad happened!")
    }

    val uri = name.tree match {
      case Literal(Constant(nameLit: String)) => schema.getOrElse(
        nameLit,
        c.abort(c.enclosingPosition, "Invalid member name!")
      )
      case _ => c.abort(
        c.enclosingPosition,
        "Invalid member name (it's not even a literal)!"
      )
    }

    reify(new java.net.URI(c.literal(uri).splice))
 //   reify(MyRecord(c.literal(uri).splice))
    //reify{case class MyRecord(x: String)}//c.literal(uri).splice)}

  }
}
//case class MyRecord(x: String)
