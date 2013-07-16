package fake_type_providers

trait SchemaParsingUtils {
  def parseLine(line: String): Either[String, (String, String)] =
    line.split(' ') match {
      case Array (k, v) => Right(k -> v)
      case _ => Left("Ill-formed schema line: " + line + "!")
    }
}

