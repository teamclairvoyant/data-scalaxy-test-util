package com.clairvoyant

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MyScalaApplicationIntegrationTest extends AnyFlatSpec with Matchers {

  "MyScalaApplication main()" should "run successfully" in {
    MyScalaApplication.main(Array(""))
  }

}
