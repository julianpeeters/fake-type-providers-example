package fake_type_providers
object Test extends App {
  val oreDyn = DynamicSchemaMaker("/schema.txt")
    println(oreDyn.proxyIn)
}
