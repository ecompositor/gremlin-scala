package com.tinkerpop.gremlin.scala

import org.scalatest.matchers.ShouldMatchers
import shapeless._

class RemoveMeSpec extends TestBase {

  it("keeps the types in the traversal") {
    //gs.V.filterBlub{v: ScalaVertex => true}
    //gs.V.filter{v: Vertex => v.scalaOnlyMethod}
    //gs.V.filter(_.scalaOnlyMethod)
    //gs.V.filter(_.value[String]("name") == "marko").toList foreach println

    import crazy._
    gs.V2.filterBlub{v =>
      println(v.value[String]("name"))
      true}.toList

    gs.V2.value[String]("name").filterBlub{s =>
      println(s)
      true}.toList
  }

  //TODO: also test with steps that don't add to the path:
  //  order, shuffle, has, filter, dedup, except, as, aggregate
  //TODO: back, jump etc.
}
