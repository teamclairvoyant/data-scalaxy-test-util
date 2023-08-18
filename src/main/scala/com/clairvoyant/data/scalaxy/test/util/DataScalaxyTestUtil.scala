package com.clairvoyant.data.scalaxy.test.util

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

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

}
