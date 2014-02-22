package com.tinkerpop.gremlin.scala

import org.scalatest.matchers.ShouldMatchers

class FilterSpec extends TestBase {

  describe("filter") {
    it("filters by name") {

      //TODO: try to simply cast it when creating VertexSteps
      //problem: tinkervertex cannot be cast to ScalaVertex, we have to convert it first
      //import shapeless._
      //val g = gs.V.asInstanceOf[GremlinScala[ScalaVertex :: HNil, ScalaVertex]]
      //g.filter{v: ScalaVertex => true}.toList foreach println

      //gs.V.value[String]("name").toList foreach println
      //gs.V.filter{ v: Vertex =>
        ////TODO: find out why v.value creates a new pipeline - does vertex/element have it?
        //println(v.value[String]("name"))
        //println(v.scala.value[String]("name"))
        //v.value[String]("name") == "marko"}.toList foreach println
      //gs.V.filter(_.value[String]("name") == "marko")
        //.value[String]("name").toList should be("marko")
    }

    it("filters with default value") {
      //TODO: always convert to ScalaVertex/Edge where possible?
      gs.V
        .filter { v: Vertex => ScalaVertex(v).value("age", default = 0) > 30 }
        //.filter(_.value("age", default = 0) > 30)
        .value[String]("name").toSet should be(Set("josh", "peter"))
    }
  }

  describe("dedup") {
    it("dedups") {
      v(1).out.in.dedup.toList should be(v(1).out.in.toSet.toList)
    }

    it("dedups by a given uniqueness function") {
      v(1).out.in
        .dedup(v => ScalaVertex(v).property[String]("lang").orElse(null))
        .value[String]("name").toList should be(List("josh"))
    }
  }

  describe("except") {
    it("emits everything but a given object") {
      v(1).out.except(v(2).vertex).value[String]("name")
        .toSet should be(Set("lop", "josh"))
    }

    it("emits everything but an 'except' list") { 
      v(1).out.except(List(v(2).vertex)).value[String]("name")
        .toSet should be(Set("lop", "josh"))
    }

    it("emits everything unless the vertex is in a given aggregate variable") {
      v(1).out.aggregate("x")
        .out.exceptVar("x")
        .value[String]("name").toSet should be (Set("ripple"))
    }

    it("emits everything unless a property is in a given aggregate variable") {
      v(1).out
        .aggregate("x", v => ScalaVertex(v).value[String]("name"))
        .out.value[String]("name").exceptVar("x")
        .toSet should be (Set("ripple"))
    }
  }
}
