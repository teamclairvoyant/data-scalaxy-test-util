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

  /**
   * Reads JSON text and parse it to DataFrame.
   *
   * @param text
   *   JSON text to parse
   * @param jsonOptions
   *   Map of spark read options for JSON format
   * @return
   *   DataFrame
   */
  def readJSONFromText(text: String, jsonOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("multiline", true)
      .option("inferSchema", true)
      .options(jsonOptions)
      .json(Seq(text).toDS())

  /**
   * Reads JSON file and parse it to DataFrame.
   *
   * @param path
   *   Path to JSON file
   * @param jsonOptions
   *   Map of spark read options for JSON format
   * @return
   *   DataFrame
   */
  def readJSONFromFile(path: String, jsonOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("multiline", true)
      .option("inferSchema", true)
      .options(jsonOptions)
      .json(path)

  /**
   * Reads CSV text and parse it to DataFrame.
   *
   * @param text
   *   CSV text to parse
   * @param csvOptions
   *   Map of spark read options for CSV format
   * @return
   *   DataFrame
   */
  def readCSVFromText(text: String, csvOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("header", true)
      .options(csvOptions)
      .csv {
        Seq(text)
          .flatMap(_.split(csvOptions.getOrElse("lineSep", "\n")))
          .toDS()
      }

  /**
   * Reads CSV file and parse it to DataFrame.
   *
   * @param path
   *   Path to CSV file
   * @param csvOptions
   *   Map of spark read options for CSV format
   * @return
   *   DataFrame
   */
  def readCSVFromFile(path: String, csvOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("header", true)
      .options(csvOptions)
      .csv(path)

  /**
   * Reads XML text and parse it to DataFrame.
   *
   * @param text
   *   XML text to parse
   * @param xmlOptions
   *   Map of spark read options for XML format
   * @return
   *   DataFrame
   */
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

  /**
   * Reads XML file and parse it to DataFrame.
   *
   * @param path
   *   Path to XML file
   * @param xmlOptions
   *   Map of spark read options for XML format
   * @return
   *   DataFrame
   */
  def readXMLFromFile(path: String, xmlOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .option("rowTag", "row")
      .options(xmlOptions)
      .format("xml")
      .load(path)

  /**
   * Reads Parquet file and parse it to DataFrame.
   *
   * @param path
   *   Path to Parquet file
   * @param parquetOptions
   *   Map of spark read options for Parquet format
   * @return
   *   DataFrame
   */
  def readParquet(path: String, parquetOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .options(parquetOptions)
      .parquet(path)

}
