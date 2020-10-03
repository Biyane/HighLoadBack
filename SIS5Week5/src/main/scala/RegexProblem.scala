import java.io.{BufferedWriter, File, PrintWriter}

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._, io.circe.parser._
import scala.io.Source
import scala.util.matching.Regex

object RegexProblem extends App {

  case class Duplicate(name: String, value: String)

  case class OverallPrice(name: String, value: String)

  case class FiscalFeature(name: String, value: String)

  case class Product(nameOfProduct: String, cost: String, totalCost: String)
//  `Продажа`: Array[Json],
  case class Answer(`Дубликаты`: List[Json], `Покупка`: Array[Product],
                    `Стоимость`: List[Json], `Фискальный признак`: List[Json])

  case class Test(`Покупка`: Array[Product])

  var cnt: Int = 0
  var innerProductCnt: Int = 0
  var innerProductArray: Array[String] = Array[String]()
  var arrayOfProductObjects: Array[Product] = Array[Product]()
  var arrayOfDuplicatePart: Array[Duplicate] = Array[Duplicate]()
  var listOfJsonObjectOfDuplicatePart: List[Json] = Nil
  var arrayOfOverallCostPart: Array[OverallPrice] = Array[OverallPrice]()
  var subStringOfOverallCostPart: String = ""
  var listOfOverallJsonObject: List[Json] = Nil
  var arrayOfFiscalFeature: Array[FiscalFeature] = Array[FiscalFeature]()
  var listOfJsonOfFiscalFuture: List[Json] = Nil
  val file_name = "BackSIS.text"
  val fSource = Source.fromFile(file_name)
  for (line <- fSource.getLines) {
    if (line == "ПРОДАЖА" || line == "Банковская карта:" || line == "Фискальный признак:") {
      cnt += 1
    }
    cnt match {
      case 0 =>
        if (line == "ДУБЛИКАТ") {}
        else {
          val tempArrOfProduct = line.split(" ")
          val arrLen = tempArrOfProduct.length
          if (tempArrOfProduct(0) == "НДС" || tempArrOfProduct(0) == "Порядковый номер") {
            var tempLine: String = ""
            for (i <- 0 until tempArrOfProduct.length - 1) tempLine += tempArrOfProduct(i)
            arrayOfDuplicatePart :+= Duplicate(tempLine, tempArrOfProduct(arrLen - 1))
          }
          else {
            var tempLine: String = ""
            for (i <- 1 until arrLen) tempLine += tempArrOfProduct(i)
            arrayOfDuplicatePart :+= Duplicate(tempArrOfProduct(0), tempLine)
          }
        }

      case 1 =>
        val regexForCounter: Regex = "([0-9.]+)".r
        if (line == "ПРОДАЖА") {}
        else if (regexForCounter matches line) {
          innerProductCnt = 0
        }
        else {
          innerProductCnt += 1
          if (innerProductCnt == 4) {
            val product = Product(innerProductArray(0), innerProductArray(1), innerProductArray(2))
            arrayOfProductObjects :+= product
          }
          else if (innerProductCnt < 4) {
            innerProductArray :+= line
          }
          else {
            innerProductCnt = 0
          }
        }

      case 2 =>
        innerProductCnt += 1
        if (innerProductCnt % 2 == 1) {
          subStringOfOverallCostPart += (line + "\n")
        }
        else if (innerProductCnt % 2 == 0) {
          subStringOfOverallCostPart += line
          if (subStringOfOverallCostPart.contains("НДС")) {
            arrayOfOverallCostPart :+= OverallPrice("НДС 12%", "0,00")
            subStringOfOverallCostPart = ""
          }
          else {
            val tempArray = subStringOfOverallCostPart.split("\n")
            arrayOfOverallCostPart :+= OverallPrice(tempArray(0), tempArray(1))
            subStringOfOverallCostPart = ""
          }
        }

      case 3 =>
        val tempRegex = "г. ([0-9\\D ]+)".r
        val tempRegex2 = "([А-Яа-я() ]+): ([0-9\\D ]+)".r
        if (tempRegex matches line) {
          for (patterMatch <- tempRegex.findAllMatchIn(line))
            arrayOfFiscalFeature :+= FiscalFeature("город", s"${patterMatch.group(1)}")
          //          val strTemp = tempRegex.findAllMatchIn(line)

        }
        else if (tempRegex2 matches line) {
          for (patterMatch <- tempRegex2.findAllMatchIn(line))
            arrayOfFiscalFeature :+= FiscalFeature(s"${patterMatch.group(1)}", s"${patterMatch.group(2)}")
          //          val strTemp = tempRegex2.findAllMatchIn(line)

        }

      case _ =>
    }
  }
  for (duplicate <- arrayOfDuplicatePart) {
    listOfJsonObjectOfDuplicatePart :+= Json.obj(
      duplicate.name -> duplicate.value.asJson
    )
  }
  for (price <- arrayOfOverallCostPart) {
    listOfOverallJsonObject :+= Json.obj(
      price.name -> price.value.asJson
    )
  }
  for (feature <- arrayOfFiscalFeature) {
    listOfJsonOfFiscalFuture :+= Json.obj(
      feature.name -> feature.value.asJson
    )
  }
  val answer = Answer(listOfJsonObjectOfDuplicatePart, arrayOfProductObjects,
    listOfOverallJsonObject, listOfJsonOfFiscalFuture)
  val new_File = new File("answer.text")
  val file_writter = new PrintWriter(new_File)
  val bw = new BufferedWriter(file_writter)
  val test = Test(arrayOfProductObjects)
  bw.write(answer.asJson.toString())
  bw.close()
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
