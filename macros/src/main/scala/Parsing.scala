package fake_type_providers

trait SchemaParsingUtils {
  def parseLine(line: String): Either[String, (String, String)] =
    line.split(' ') match {
      case Array (k, v) => Right(k -> v)
      case _ => Left("Ill-formed schema line: " + line + "!")
    }
}

import scala.io.Source

object CodeGenSchemaMaker extends SchemaParsingUtils {
  def apply(path: String, name: String) = {
    val stream = Option(this.getClass.getResourceAsStream(path)).getOrElse(
      sys.error("Invalid resource path!")
    )

    val vals = Source.fromInputStream(stream).getLines.map(
      parseLine(_).fold(
        sys.error(_),
        { case (k, v) => "  val " + k + " = new URI(\"" + v + "\")\n" }
      )
    ).mkString

    s"object $name extends Schema {\n  import java.net.URI\n$vals}"
  }
}
