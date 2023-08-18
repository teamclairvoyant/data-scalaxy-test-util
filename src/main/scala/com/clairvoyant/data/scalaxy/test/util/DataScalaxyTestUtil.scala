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

  def readJSON(text: String): DataFrame =
    sparkSession.read
      .option("multiline", value = true)
      .option("inferSchema", value = true)
      .json(Seq(text).toDS())

  def readCSVFromText(text: String, csvOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .options(csvOptions)
      .csv {
        Seq(text)
          .flatMap(_.split(csvOptions.getOrElse("lineSep", "\n")))
          .toDS()
      }

  def readCSVFromFile(path: String, csvOptions: Map[String, String] = Map.empty): DataFrame =
    sparkSession.read
      .options(csvOptions)
      .csv(path)

}
