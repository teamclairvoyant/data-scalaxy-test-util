# data-scalaxy-test-util

This library provides additional APIs to support testing frameworks for spark scala projects.

## Getting Started

### Add SBT dependency

To use `data-scalaxy-test-util` in an existing SBT project with Scala 2.12 or a later version,
add the following dependency to your `build.sbt`

```sbt
ThisBuild / resolvers += "Github Repo" at "https://maven.pkg.github.com/teamclairvoyant/data-scalaxy-test-util/"

ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  System.getenv("GITHUB_USERNAME"),
  System.getenv("GITHUB_TOKEN")
)

ThisBuild / libraryDependencies += "com.clairvoyant" %% "data-scalaxy-test-util" % "1.0.0" % Test
```

Make sure you add `GITHUB_USERNAME` and `GITHUB_TOKEN` to the environment variables.

`GITHUB_TOKEN` is the Personal Access Token with the permission to read packages.

## Features:

### 1. Spark DataFrame Comparison

This library exposes APIs that can be used to compare two spark dataframes.

This feature is very much helpful in writing unit tests and integration tests for data scalaxy projects where two 
dataframes need to be compared for equality.

The below comparisons are made against two dataframes:

* Validate Columns
* Validate Size
* Validate Schema
* Validate Rows

#### Usage Examples

Consider a unit test where we have actual dataframe and expected dataframe. Now in order to compare these two dataframes, 
you need to use the api in the below manner:

###### Example-1

For the use case, where we have exactly same dataframes, the below test case will successfully pass.

```scala
import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class DataFrameMatchersTest extends DataScalaxyTestUtil {
  "matchExpectedDataFrame() - with 2 exact dataframes" should "compare two dataframes correctly" in {
    val df1 = readJSON(
      """{
        |  "col_A": "val_A",
        |  "col_B": "val_B"
        |}""".stripMargin
    )

    val df2 = df1

    df1 should matchExpectedDataFrame(df2)
  }
}
```

###### Example-2

For the use case, where we have two dataframes having different columns, the below test case will fail with the error message:

```text
Content of data frame does not match expected data.
* Actual DF has different columns than Expected DF
Actual DF columns: col_A,col_B
Expected DF columns: col_A,col_C
Extra columns: col_B
Missing columns col_C
```

```scala
import com.clairvoyant.data.scalaxy.test.util.DataScalaxyTestUtil

class DataFrameMatchersTest extends DataScalaxyTestUtil {
  "matchExpectedDataFrame() - with 2 dataframes having different columns" should "fail dataframes comparison" in {
    val df1 = readJSON(
      """{
        |  "col_A": "val_A",
        |  "col_B": "val_B"
        |}""".stripMargin
    )

    val df2 = readJSON(
      """{
        |  "col_A": "val_A",
        |  "col_C": "val_B"
        |}""".stripMargin
    )

    df1 should matchExpectedDataFrame(df2)
  }
}
```

Please refer to [examples](https://github.com/teamclairvoyant/data-scalaxy-test-util/blob/master/src/test/scala/com/clairvoyant/data/scalaxy/test/util/DataFrameMatchersTest.scala) for various use cases where you can use this library to compare two dataframes.

### 2. APIs to read data of several formats and parse it to spark dataframe

This library provides below APIs:

* readJSONFromText
* readJSONFromFile
* readCSVFromText
* readCSVFromFile
* readXMLFromText
* readXMLFromFile

You can find the documentation for each API [here](src/main/scala/com/clairvoyant/data/scalaxy/test/util/DataScalaxyTestUtil.scala).
