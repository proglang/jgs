package de.unifreiburg.cs.proglang.jgs.jimpleutils;

class CastsFromConstantsTest extends FlatSpec with Matchers{
  import CastsFromConstantsTest._

  val casts = new CastsFromConstants(types,"<testCasts: java.lang.Object cast(java.lang.String,java.lang.Object)>",
          "<testCasts: void castCx(java.lang.String)>", "<testCasts: void castCxEnd()>")


  "valueCastMethod.toString" should "be <testCasts: java.lang.Object cast(java.lang.String,java.lang.Object)>" in {
    valueCastMethod.toString should be ("<testCasts: java.lang.Object cast(java.lang.String,java.lang.Object)>")
  }

  "cast(LOW ~> HIGH, x)" should "convert from LOW to HIGH" in {
    casts.detectValueCastFromCall(lowToHigh) should be (Success(Some(new ValueCast[LowHigh.Level](TLOW, THIGH, Some(varX)))))
  }

  it should "not be a context casts" in {
    casts.detectContextCastStartFromCall(lowToHigh) should be (Success(None))
    casts.detectContextCastEndFromCall(lowToHigh) should be (false)
  }

  "cast(y,x)" should "throw an exception" in {
    an [IllegalArgumentException] should be thrownBy casts.detectValueCastFromCall(wrongLowToHigh).get

  }

  "cxCast(? ~> LOW)" should "convert cx from ? to LOW" in {
    casts.detectContextCastStartFromCall(cxDynToLow) should be (Success(Some (new CxCast[LowHigh.Level](DYN, TLOW))))
  }

  it should "not be a value cast or cx-cast end" in {
    casts.detectValueCastFromCall(cxDynToLow)  should be (Success(None))
    casts.detectContextCastEndFromCall(cxDynToLow) should be (false)
  }

  "cxCaseEnd" should "be a cx cast end" in {
    casts.detectContextCastEndFromCall(cxEnd) should be (true)
  }

  it should "not be a value cast or context cast" in {
    casts.detectValueCastFromCall(cxEnd) should be (Success(None))
    casts.detectContextCastStartFromCall(cxEnd) should be (Success(None))
  }

}
