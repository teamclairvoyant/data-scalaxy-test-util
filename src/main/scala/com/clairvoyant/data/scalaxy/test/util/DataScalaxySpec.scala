package com.clairvoyant.data.scalaxy.test.util

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait DataScalaxySpec extends AnyFlatSpec with Matchers with DataFrameMatchers {

  val sparkSession: SparkSession = SparkSession
    .builder()
    .master("local[*]")
    .getOrCreate()

  import sparkSession.implicits.*

  def readJSON(text: String, schema: Option[StructType] = None): DataFrame = {
    val dataFrameReader = sparkSession.read
      .option("multiline", value = true)
      .option("inferSchema", value = schema.isEmpty)

    schema
      .map { schema => dataFrameReader.schema(schema) }
      .getOrElse(dataFrameReader)
      .json(Seq(text).toDS())
  }

}
