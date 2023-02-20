package application

object Parser {

  val parseKeyValueFromCSVLine: PartialFunction[String, KeyValue] =
    parseKeyValueTupleFromDelimitedLine(',')

  val parseKeyValueFromTSVLine: PartialFunction[String, KeyValue] =
    parseKeyValueTupleFromDelimitedLine('\t')

  def parseKeyValueTupleFromDelimitedLine(delimiter: Char): PartialFunction[String, KeyValue] = {
    case line if line.forall(str => str.isDigit || str == delimiter) =>
      line.split(s"$delimiter") match {
        case Array(key, value) =>
          (key.toLong, value.toLong)
      }
  }

}
