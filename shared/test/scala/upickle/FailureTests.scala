package upickle

import utest._

/**
* Generally, every failure should be a Invalid.Json or a
* InvalidData. If any assertion errors, match errors, number
* format errors or similar leak through, we've failed
*/
object FailureTests extends TestSuite{

  def tests = TestSuite {
    'jsonFailures {
      // Run through the test cases from the json.org validation suite,
      // skipping the ones which we don't support yet (e.g. leading zeroes,
      // extra commas) or will never support (e.g. too deep)

      val failureCases = Seq(
//        """ "A JSON payload should be an object or array, not a string." """,
        """ {"Extra value after close": true} "misplaced quoted value" """,
        """ {"Illegal expression": 1 + 2} """,
        """ {"Illegal invocation": alert()} """,
//        """ {"Numbers cannot have leading zeroes": 013} """,
        """ {"Numbers cannot be hex": 0x14} """,
        """ ["Illegal backslash escape: \x15"] """,
        """ [\naked] """,
        """ ["Illegal backslash escape: \017"] """,
//        """ [[[[[[[[[[[[[[[[[[[["Too deep"]]]]]]]]]]]]]]]]]]]] """,
        """ {"Missing colon" null} """,
        """ ["Unclosed array" """,
        """ {"Double colon":: null} """,
        """ {"Comma instead of colon", null} """,
        """ ["Colon instead of comma": false] """,
        """ ["Bad value", truth] """,
        """ ['single quote'] """,
        """ ["	tab	character	in	string	"] """,
        """ ["tab\   character\   in\  string\  "] """,
        """ ["line
          break"] """,
        """ ["line\
          break"] """,
        """ [0e] """,
        """ {unquoted_key: "keys must be quoted"} """,
        """ [0e+-1] """,
        """ {"Comma instead if closing brace": true, """,
        """ ["mismatch"} """,
//        """ ["extra comma",] """,
        """ ["double extra comma",,] """,
        """ [   , "<-- missing value"] """,
        """ ["Comma after the close"], """,
        """ ["Extra close"]] """
//        """ {"Extra comma": true,} """
      ).map(_.trim())
      for(failureCase <- failureCases){
//        println(failureCase)
        intercept[Invalid.Json] { read[Int](failureCase) }
      }
    }
    'otherFailures{
      intercept[Invalid.Data] { read[Boolean]("\"lol\"") }
      intercept[Invalid.Data] { read[Int]("\"lol\"") }
      intercept[Invalid.Data] { read[Seq[Int]]("\"lol\"") }
      intercept[Invalid.Data] { read[Seq[String]]("[1, 2, 3]") }
      intercept[Invalid.Data] { read[Seq[(Int, String)]]("[[1, \"1\"], [2, \"2\"], []]") }
    }
  }
}