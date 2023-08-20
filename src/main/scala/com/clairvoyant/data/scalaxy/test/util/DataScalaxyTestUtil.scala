package com.clairvoyant.data.scalaxy.test.util

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{File, PrintWriter}
import java.util.concurrent.Semaphore

trait DataScalaxyTestUtil extends AnyFlatSpec with Matchers with DataFrameMatchers {

  given sparkSession: SparkSession =
    SparkSession
      .builder()
      .master("local[*]")
      .getOrCreate()

  import sparkSession.implicits.*

  def readJSONFromText(text: String, jsonOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("multiline", true)
      .option("inferSchema", true)
      .options(jsonOptions)
      .json(Seq(text).toDS())

  def readJSONFromFile(path: String, jsonOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("multiline", true)
      .option("inferSchema", true)
      .options(jsonOptions)
      .json(path)

  def readCSVFromText(text: String, csvOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("header", true)
      .options(csvOptions)
      .csv {
        Seq(text)
          .flatMap(_.split(csvOptions.getOrElse("lineSep", "\n")))
          .toDS()
      }

  def readCSVFromFile(path: String, csvOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("header", true)
      .options(csvOptions)
      .csv(path)

  def readXMLFromText(text: String, xmlOptions: Map[String, String] = Map.empty): DataFrame = {
    def saveXMLTextToTempFiles(xmlTextValue: Seq[String]) = {
      xmlTextValue.map { text =>
        val file = File.createTempFile("xml-text-", ".xml")
        file.deleteOnExit()
        new PrintWriter(file) {
          try {
            write(text)
          } finally {
            close()
          }
        }
        file
      }
    }

    val semaphore = new Semaphore(1)
    semaphore.acquire()

    val tempXMLFiles = saveXMLTextToTempFiles(Seq(text))
    val tempXMLFilesPaths = tempXMLFiles.map(_.getAbsolutePath).mkString(",")

    val xmlDataFrame = sparkSession.read
      .option("rowTag", "row")
      .options(xmlOptions)
      .format("xml")
      .load(tempXMLFilesPaths)

    semaphore.release()

    xmlDataFrame
  }

  def readXMLFromFile(path: String, xmlOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("rowTag", "row")
      .options(xmlOptions)
      .format("xml")
      .load(path)

}
