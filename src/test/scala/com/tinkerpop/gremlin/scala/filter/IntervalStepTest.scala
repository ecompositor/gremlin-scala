package com.tinkerpop.gremlin.scala.filter

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import com.tinkerpop.gremlin.Tokens
import com.tinkerpop.gremlin.Tokens.T
import com.tinkerpop.gremlin.scala.TestGraph

class IntervalStepTest extends FunSpec with ShouldMatchers with TestGraph {

  it("finds everybody between 20 and 30 years old") {
    graph.V.interval("age", 20, 30).toList.size should be(2)
  }

}
