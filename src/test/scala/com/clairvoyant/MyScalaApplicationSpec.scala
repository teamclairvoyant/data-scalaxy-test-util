package com.clairvoyant

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MyScalaApplicationSpec extends AnyFlatSpec with Matchers {

  "add()" should "add two numbers correctly" in {
    val calculator = new MyCalculator

    calculator.add(10, 5) shouldBe 15
  }

}
