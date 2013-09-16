package com.foursquare.lint

sealed trait Warning {
  def message: String
  def name: String
}

object Warning {
  final val All: List[Warning] = 
    UnextendedSealedTrait ::
    UseLog1p ::
    UseExpm1 ::
    new UnlikelyEquality("", "") ::
    Nil 

  final val AllNames = All.map(_.name)

  final val NameToWarning: Map[String, Warning] = All.map(w => w.name -> w).toMap
}

sealed trait DefaultNameWarning extends Warning {
  def name = toString
}

sealed abstract class NoArgMessageWarning(val message: String) extends DefaultNameWarning

sealed abstract class TwoArgMessageWarning(format: String, arg1: String, arg2: String) extends Warning {
  def message = format.format(arg1, arg2)
}

case object UnextendedSealedTrait extends NoArgMessageWarning("This sealed trait is never extended")

case object UseLog1p extends NoArgMessageWarning("Use math.log1p(x) instead of math.log(1 + x) for added accuracy when x is near 0")

case object UseExpm1 extends NoArgMessageWarning("Use math.expm1(x) instead of math.exp(x) - 1 for added accuracy when x is near 0.")

class UnlikelyEquality(lhs: String, rhs: String) extends 
  TwoArgMessageWarning("Comparing with == on instances of different types (%s, %s) will probably return false.", lhs, rhs) {
  def name = "UnlikelyEquality"
}
