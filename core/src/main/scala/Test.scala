package fake_type_providers
import scala.reflect.runtime.universe._
import scala.tools.scalap.scalax.rules.scalasig._

import com.novus.salat._
import com.novus.salat.global._

object Test extends App {
  val oreDyn = DynamicSchemaMaker("/schema.txt")
  val proxyIn = oreDyn.proxyIn
  type ProxyIn = proxyIn.type //with CaseClass

println(oreDyn)
println(oreDyn.getClass)

println(proxyIn)
println(proxyIn.getClass)


println(typeOf[ProxyIn])


//val parser = ScalaSigParser.parse(proxyIn.getClass)
//val parser = ScalaSigParser.parse(classOf[ProxyIn])//error class type required
//println("parser: " + parser)

// val dbo = grater[ProxyIn].asDBObject(proxyIn)

  //  println(dbo)

 // val obj = grater[ProxyIn].asObject(dbo)
  //  println(obj)
 
 // println(proxyIn == obj)
  // println(typeTemplate == obj)

}

