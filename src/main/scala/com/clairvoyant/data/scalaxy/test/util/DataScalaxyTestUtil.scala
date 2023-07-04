package com.clairvoyant.data.scalaxy.test.util

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait DataScalaxyTestUtil extends AnyFlatSpec with Matchers with DataFrameMatchers {

  val sparkSession: SparkSession = SparkSession
    .builder()
    .master("local[*]")
    .getOrCreate()

  import sparkSession.implicits.*

  def readJSON(text: String): DataFrame =
    sparkSession.read
      .option("multiline", value = true)
      .option("inferSchema", value = true)
      .json(Seq(text).toDS())

}
