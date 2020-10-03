import java.io.{BufferedWriter, File, PrintWriter}

import RegexProblem.arrayOfDuplicatePart
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.parser._

import scala.io.Source
import scala.util.matching.Regex

object RegexProblem extends App {

  case class Duplicate(name: String, value: String)

  case class OverallPrice(name: String, value: String)

  case class FiscalFeature(name: String, value: String)

  case class Product(nameOfProduct: String, cost: String, totalCost: String)

  case class Answer(`Дубликаты`: Json, `Покупка`: Array[Product],
                    `Стоимость`: Json, `Фискальный признак`: Json)

  case class Test(`Покупка`: Array[Product])

  var cnt: Int = 0
  var innerProductCnt: Int = 0
  var innerProductArray: Array[String] = Array[String]()
  var arrayOfProductObjects: Array[Product] = Array[Product]()
  var arrayOfDuplicatePart: Array[Duplicate] = Array[Duplicate]()
  var arrayOfOverallCostPart: Array[OverallPrice] = Array[OverallPrice]()
  var subStringOfOverallCostPart: String = ""
  var arrayOfFiscalFeature: Array[FiscalFeature] = Array[FiscalFeature]()
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

        }
        else if (tempRegex2 matches line) {
          for (patterMatch <- tempRegex2.findAllMatchIn(line))
            arrayOfFiscalFeature :+= FiscalFeature(s"${patterMatch.group(1)}", s"${patterMatch.group(2)}")
        }

      case _ =>
    }
  }

  val answerDuplicate: Json = Json.obj(
    arrayOfDuplicatePart(0).name -> arrayOfDuplicatePart(0).value.asJson,
    arrayOfDuplicatePart(1).name -> arrayOfDuplicatePart(1).value.asJson,
    arrayOfDuplicatePart(2).name -> arrayOfDuplicatePart(2).value.asJson,
    arrayOfDuplicatePart(3).name -> arrayOfDuplicatePart(3).value.asJson,
    arrayOfDuplicatePart(4).name -> arrayOfDuplicatePart(4).value.asJson,
    arrayOfDuplicatePart(5).name -> arrayOfDuplicatePart(5).value.asJson,
    arrayOfDuplicatePart(6).name -> arrayOfDuplicatePart(6).value.asJson,
    arrayOfDuplicatePart(7).name -> arrayOfDuplicatePart(7).value.asJson,
    arrayOfDuplicatePart(8).name -> arrayOfDuplicatePart(8).value.asJson,
  )
  val answerOverallObject: Json = Json.obj(
    arrayOfOverallCostPart(0).name -> arrayOfOverallCostPart(0).value.asJson,
    arrayOfOverallCostPart(1).name -> arrayOfOverallCostPart(1).value.asJson,
    arrayOfOverallCostPart(2).name -> arrayOfOverallCostPart(2).value.asJson,

  )
  val answerFiscalFeatureObject: Json = Json.obj(
    arrayOfFiscalFeature(0).name -> arrayOfFiscalFeature(0).value.asJson,
    arrayOfFiscalFeature(1).name -> arrayOfFiscalFeature(1).value.asJson,
    arrayOfFiscalFeature(2).name -> arrayOfFiscalFeature(2).value.asJson,
    arrayOfFiscalFeature(3).name -> arrayOfFiscalFeature(3).value.asJson,
    arrayOfFiscalFeature(4).name -> arrayOfFiscalFeature(4).value.asJson,
    arrayOfFiscalFeature(5).name -> arrayOfFiscalFeature(5).value.asJson
  )
  val answer = Answer(answerDuplicate, arrayOfProductObjects,
    answerOverallObject, answerFiscalFeatureObject)
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
