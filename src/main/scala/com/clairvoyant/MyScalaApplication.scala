package com.clairvoyant

object MyScalaApplication extends App {
  val calculator = new MyCalculator

  print(calculator.add(10, 5))
}

class MyCalculator {
  def add(a: Int, b: Int): Int = a + b
}
