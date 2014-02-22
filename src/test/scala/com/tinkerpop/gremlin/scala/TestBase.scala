package com.tinkerpop.gremlin.scala

import com.tinkerpop.gremlin.structure._
import com.tinkerpop.tinkergraph.TinkerFactory
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec

trait TestGraph {
  val graph = TinkerFactory.createClassic()
  def gs: ScalaGraph = GremlinScala.of(graph)
  def v(i: Int): ScalaVertex = gs.v(i:Integer).get
  def e(i: Int): ScalaEdge = gs.e(i:Integer).get

  def print(gs: GremlinScala[_,_]) = println(gs.toList)
}

trait TestBase extends FunSpec with ShouldMatchers with TestGraph {
  implicit class Properties[A](set: Traversable[Property[A]]) {
    def unroll(): Traversable[A] = set map (_.get)
  }
}
