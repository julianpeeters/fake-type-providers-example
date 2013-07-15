package fake_type_providers

import scala.language.experimental.macros
import scala.reflect.macros.Context

trait ReflectionUtils {
  def constructor(u: scala.reflect.api.Universe) = {
    import u._
 
    DefDef(
      Modifiers(),
      nme.CONSTRUCTOR,
      Nil,
      Nil :: Nil,
      TypeTree(),
      Block(
        Apply(
          Select(Super(This(tpnme.EMPTY), tpnme.EMPTY), nme.CONSTRUCTOR),
          Nil
        ) :: Nil,
        Literal(Constant(()))
      )
    )
  }
}
