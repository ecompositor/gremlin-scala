package com.tinkerpop.gremlin.scala

import com.tinkerpop.blueprints._
import com.tinkerpop.gremlin.scala._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import com.tinkerpop.gremlin.Tokens.T._
import scala.collection.JavaConversions._
import scala.collection.mutable
import java.util.{ Map ⇒ JMap, HashMap ⇒ JHashMap, Collection ⇒ JCollection }
import com.tinkerpop.pipes.util.structures.Tree

class SideEffectTest extends FunSpec with ShouldMatchers with TestGraph {

  describe("aggregate") {
    it("fills a buffer greedily") {
      val buffer = mutable.Buffer.empty[Vertex]
      graph.v(1).out.aggregate(buffer).iterate()
      List(2, 3, 4) foreach { i ⇒
        buffer.toSet.contains(graph.v(i)) should be(true)
      }
    }

    it("applies a transformation to each element before filling the buffer") {
      val buffer = mutable.Buffer.empty[String]
      graph.v(1).out.aggregate(buffer)(getName).iterate()

      buffer should be(mutable.Buffer("vadas", "josh", "lop"))
    }
  }

  describe("store") {
    it("fills a buffer lazily") {
      val buffer = mutable.Buffer.empty[Vertex]
      graph.v(1).out.store(buffer).iterate()
      List(2, 3, 4) foreach { i ⇒
        buffer.toSet.contains(graph.v(i)) should be(true)
      }
    }

    it("applies a transformation to each element before filling the buffer") {
      val buffer = mutable.Buffer.empty[String]
      graph.v(1).out.store[String](buffer, getName).iterate()

      buffer should be(mutable.Buffer("vadas", "josh", "lop"))
    }
  }

  describe("groupBy") {
    it("groups tinkerpop team by age range") {
      //      val ageMap = mutable.Map.empty[Integer, mutable.Buffer[Vertex]]
      val ageMap = new JHashMap[String, JCollection[Any]]
      graph.V.groupBy(ageMap)(keyFunction = ageRange, valueFunction = getName).iterate()

      val result = ageMap.toMap
      result(underThirty).toList should be(List("vadas", "marko"))
      result(overThirty).toList should be(List("peter", "josh"))
      result(unknown).toList should be(List("lop", "ripple"))
    }
  }

  describe("tree") {
    it("stores the tree formed by the traversal as a map") {
      val tree = graph.v(1).out.out.tree.cap.toList.head.asInstanceOf[Tree[Vertex]]
      tree.keys should be(Set(v1))
      tree.toString should be("{v[1]={v[4]={v[3]={}, v[5]={}}}}")
      //TODO: reimplement with proper types and proper test
    }
  }

  def getName(v: Vertex) = v.getProperty[String]("name")
  def getAge(v: Vertex) = v.getProperty[Integer]("age")

  val underThirty = "under thirty"
  val overThirty = "over thirty"
  val unknown = "unknown"

  def ageRange(v: Vertex): String =
    v.property[Integer]("age") match {
      case Some(age) if (age < 30)  ⇒ underThirty
      case Some(age) if (age >= 30) ⇒ overThirty
      case _                        ⇒ unknown
    }
}
