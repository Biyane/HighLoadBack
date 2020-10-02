import java.io.File
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex
object SIS5Tester extends App{
//  val temp = Future[Int]{42}
//  val file = new File("abc.text")
//  val fileName = "abc.text"
//  val fSource = Source.fromFile(fileName)
//  while (fSource.hasNext){
//    val b: Char = fSource.next()
//    if (b == 'e')
//      print("GoodTime")
//    print(b)
//  }
//  fSource.close()
  //  val print_writter = new PrintWriter(file)
  //  print_writter.write("Hello from here")
  //  print_writter.close()
  //  val numberPat: Regex = "([2-9b-yS-Z])".r
  //  val b = numberPat.findFirstMatchIn("omeWome1")
  //   b match {
  //    case Some(_) => println(b)
  //    case _ => println("ok pass")
  //  }
  //  val a: Some[Int] = Some(5)
  //  println("a is = " , a.get)
  //  var b: Option[String] = Option(null)
  //  b = Some("sdfjsdf")
  //  println(b.get)
  //  val c = Some(null)
  //  println(c, c.get)

  val keyValPattern: Regex = "([0-9a-zA-Z- ]+): ([0-9a-zA-Z-#()/ ]+)".r

  val input: String =
    """
      |background-color #A03300 ajgdasjhgsajhgasj 3414 123;
      |background-image: url(img/header100.png);
      |background-position: top center;
      |background-repeat: repeat-x;
      |background-size: 2160px 108px;
      |margin: 0;
      |height: 108px;
      |width: 100%;""".stripMargin

  val input2: String =
    """
      |ДУБЛИКАТ
      |Филиал ТОО EUROPHARMA Астана
      |БИН 080841000761
      |НДС Серия 58001
      | № 0014377
      |Касса 300-189
      |Смена 10
      |Порядковый номер чека №64
      |Чек №2331180266
      |Кассир Аптека 17-1""".stripMargin

//  for(patterMatching <- keyValPattern.findPrefixOf(input).isDefined) println(patterMatching)
}
