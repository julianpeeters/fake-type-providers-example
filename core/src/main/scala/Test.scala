package fake_type_providers
import scala.reflect.runtime.universe._
import scala.tools.scalap.scalax.rules.scalasig._
import com.novus.salat.annotations.util._

import scala.reflect._

import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import com.gensler.scalavro.types.AvroType
import com.gensler.scalavro.io.AvroTypeIO
import scala.util.{Try, Success, Failure}

object Test extends App {
  val oreDyn = DynamicSchemaMaker("/schema.txt")
  val proxyIn = oreDyn.proxyIn
  type ProxyIn = proxyIn.type
  type oreDyn = oreDyn.type //with CaseClass

println(oreDyn)
println(oreDyn.getClass)

println(proxyIn)
println(proxyIn.getClass)


println(typeOf[ProxyIn].members)
println(typeOf[oreDyn].members)


//Dynamically generated class does not work with Salat: wrong underlying type and no sig
val cls = proxyIn.getClass
val parser = ScalaSigParser.parse(cls)
println("parser: " + parser)
println(cls.annotation[ScalaSignature])
  //val dbo = grater[ProxyIn with CaseClass].asDBObject(proxyIn)//error

//Dynamically generated class does not work with Scalavro:  no Java class corresponding to fake_type_providers.Test.proxyIn.type found
 // val myRecordType = AvroType[ProxyIn]
   // println("schema: " + myRecordType.schema)

}

