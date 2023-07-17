package com.clairvoyant.data.scalaxy.test.util

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
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

}
