package com.clairvoyant.data.scalaxy.test.util

import org.scalatest.exceptions.TestFailedException

class DataFrameMatchersTest extends DataScalaxyTestUtil {

  "matchExpectedDataFrame() - with 2 exact dataframes" should "compare two dataframes correctly" in {
    val df1 = readJSONFromText(
      """{
        |  "col_A": "val_A",
        |  "col_B": "val_B"
        |}""".stripMargin
    )

    val df2 = df1

    df1 should matchExpectedDataFrame(df2)
  }

  "matchExpectedDataFrame() - with 2 dataframes having different columns" should "fail dataframes comparison" in {
    val df1 = readJSONFromText(
      """{
        |  "col_A": "val_A",
        |  "col_B": "val_B"
        |}""".stripMargin
    )

    val df2 = readJSONFromText(
      """{
        |  "col_A": "val_A",
        |  "col_C": "val_B"
        |}""".stripMargin
    )

    lazy val assertion = df1 should matchExpectedDataFrame(df2)

    val thrownException = the[TestFailedException] thrownBy assertion

    thrownException.getMessage should equal(
      """Content of data frame does not match expected data.
        |* Actual DF has different columns than Expected DF
        |Actual DF columns: col_A,col_B
        |Expected DF columns: col_A,col_C
        |Extra columns: col_B
        |Missing columns col_C
        |""".stripMargin
    )
  }

  "matchExpectedDataFrame() - with 2 dataframes having different sizes" should "fail dataframes comparison" in {
    val df1 = readJSONFromText(
      """{
        |  "col_A": "val_A",
        |  "col_B": "val_B"
        |}""".stripMargin
    )

    val df2 = readJSONFromText(
      """[
        | {
        |   "col_A": "val_A1",
        |   "col_B": "val_B1"
        | },
        | {
        |   "col_A": "val_A2",
        |   "col_B": "val_B2"
        | }
        |]""".stripMargin
    )

    lazy val assertion = df1 should matchExpectedDataFrame(df2)

    val thrownException = the[TestFailedException] thrownBy assertion

    thrownException.getMessage should equal(
      """Content of data frame does not match expected data.
        |* Size of actual DF (1) does not match size of expected DF (2)
        |""".stripMargin
    )
  }

  "matchExpectedDataFrame() - with 2 dataframes having different schemas" should "fail dataframes comparison" in {
    val df1 = readJSONFromText(
      """{
        |  "col_A": "val_A",
        |  "col_B": "val_B"
        |}""".stripMargin
    )

    val df2 = readJSONFromText(
      """{
        |  "col_A": "val_A",
        |  "col_B": 1
        |}""".stripMargin
    )

    lazy val assertion = df1 should matchExpectedDataFrame(df2)

    val thrownException = the[TestFailedException] thrownBy assertion

    thrownException.getMessage should equal(
      """Content of data frame does not match expected data.
        |* Actual DF has different column types than Expected DF
        |Actual DF columns: root
        |-- col_B: string (nullable = true)
        |
        |Expected DF columns: root
        |-- col_B: long (nullable = true)
        |
        |Non matching columns: List((col_B STRING,col_B BIGINT))
        |""".stripMargin
    )
  }

  "matchExpectedDataFrame() - with 2 dataframes having different records" should "fail dataframes comparison" in {
    val df1 = readJSONFromText(
      """{
        |  "col_A": "val_A",
        |  "col_B": "val_B"
        |}""".stripMargin
    )

    val df2 = readJSONFromText(
      """{
        |  "col_A": "val_C",
        |  "col_B": "val_D"
        |}""".stripMargin
    )

    lazy val assertion = df1 should matchExpectedDataFrame(df2)

    val thrownException = the[TestFailedException] thrownBy assertion

    thrownException.getMessage should equal(
      """Content of data frame does not match expected data.
        |* Row: 0, field: col_A: val_A (class java.lang.String) does not match expected val_C (class java.lang.String)
        |* Row: 0, field: col_B: val_B (class java.lang.String) does not match expected val_D (class java.lang.String)
        |""".stripMargin
    )
  }

}
