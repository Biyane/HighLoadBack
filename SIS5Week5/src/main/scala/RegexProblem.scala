import java.io.File
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex

object RegexProblem extends App {

  case class Duplicate(name: String, value: String)

  case class Product(nameOfProduct: String, cost: String, totalCost: String)

  case class OverallPrice()

  case class FiscalFeature()

  case class Answer(`Дубликаты`: Array[Duplicate], `Продажа`: Array[Product],
                    `Стоимость`: Array[OverallPrice], `Фискальный признак`: Array[FiscalFeature])

  case class Test(`Покупка`: Array[Product])

  var cnt: Int = 0
  var innerProductCnt: Int = 0
  var innerProductArray: Array[String] = Array[String]()
  var arrayOfProductObjects: Array[Product] = Array[Product]()
  var arrayOfDuplicatePart: Array[Duplicate] = Array[Duplicate]()
  var arrayOfJsonObjectOfDuplicatePart: List[Json] = Nil
  val file_name = "BackSIS.text"
  val fSource = Source.fromFile(file_name)
  for (line <- fSource.getLines) {
    if (line == "ПРОДАЖА" || line == "Банковская карта:" || line == "Фискальный признак") {
      cnt += 1
    }
    cnt match {
      case 0 => {
        if (line == "ДУБЛИКАТ"){}
        else {
          val tempArrOfProduct = line.split(" ")
          val arrLen = tempArrOfProduct.length
          if (tempArrOfProduct(0) == "НДС" || tempArrOfProduct(0) == "Порядковый номер"){
            var tempLine:String = ""
            for (i <- 0 until tempArrOfProduct.length - 1) tempLine += tempArrOfProduct(i)
            arrayOfDuplicatePart :+= Duplicate(tempLine, tempArrOfProduct(arrLen - 1))
          }
          else {
            var tempLine:String = ""
            for (i <- 1 until arrLen) tempLine += tempArrOfProduct(i)
            arrayOfDuplicatePart :+= Duplicate(tempArrOfProduct(0), tempLine)
          }
        }
      }
      case 1 => {
        val regexForCounter: Regex = "([0-9.]+)".r
        if (line == "ПРОДАЖА") {}
        else if (regexForCounter matches (line)) {
          innerProductCnt = 0
        }
        else {
          innerProductCnt += 1
          if (innerProductCnt == 4) {
            val product = Product(innerProductArray(0), innerProductArray(1), innerProductArray(2))
            arrayOfProductObjects :+= product
            println(innerProductArray(0), innerProductArray(1), innerProductArray(2))
          }
          else if (innerProductCnt < 4) {
            innerProductArray :+= line
          }
          else {}
        }
      }
      case 2 =>
      case _ =>
    }
  }
  for(duplicate <- arrayOfDuplicatePart){
      arrayOfJsonObjectOfDuplicatePart  :+= Json.obj(
        duplicate.name -> duplicate.value.asJson
      )
  }
  val test = Test(arrayOfProductObjects).asJson
    println(arrayOfJsonObjectOfDuplicatePart)
}

//val regex: Regex = "([0-9a-zA-Zа-я-А-Я%#.,]+)".r

//  val regex2: Regex = "\\d.".r
//
//  for (line <- fSource.getLines) {
//    if (regex2.findFirstMatchIn(line).isDefined){}
//    else println(line)
//  }
//  println(arr.asJson)
//  var someStr: String = " № 0014377"
//  var someStr2: String = "hello from Irvana"
//  var cnt: Int = 0
//  var strArr: Array[String] = Array[String]()
//  var arr2 = someStr.split(" ")
//  var arr3 = someStr2.split(" ")
//  println(arr2(0), arr2(1), arr2(2))
//  println(arr3(0), arr3(1))
