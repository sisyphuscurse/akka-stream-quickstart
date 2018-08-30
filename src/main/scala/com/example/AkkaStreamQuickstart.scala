package com.example

import java.nio.file.Paths

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.kafka.ConsumerMessage.CommittableOffset
import akka.stream.scaladsl.{FileIO, Flow, GraphDSL, Keep, Partition, Sink, Source}
import akka.stream.{ActorMaterializer, FanOutShape2, FlowShape, Graph}
import akka.util.ByteString

import scala.concurrent.Future

object AkkaStreamQuickstart extends App {
  implicit val system = ActorSystem("QuickStart")
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()


  private def success[B, E, A]: Either[E, B] => B = _.right.get

  private def failure[B, E, A]: Either[E, B] => E = _.left.get

  private def failed[B, E, A]: Either[E, B] => Boolean = _.isLeft

  private def succeed[B, E, A]: Either[E, B] => Boolean = negate compose failed

  private def negate: Boolean => Boolean = !_

  def either[A, E, B](f: A => Either[E, B]) = Flow[A].map(f)

  object PartitionEither {
    def apply[A, B](): Graph[FanOutShape2[Either[A, B], A, B], NotUsed] =
      GraphDSL.create[FanOutShape2[Either[A, B], A, B]]() { implicit builder: GraphDSL.Builder[NotUsed] ⇒
        import GraphDSL.Implicits._

        val left = builder.add(Flow[Either[A, B]].map(_.left.get))
        val right = builder.add(Flow[Either[A, B]].map(_.right.get))
        val partition = builder.add(Partition[Either[A, B]](2, _.fold(_ ⇒ 0, _ ⇒ 1)))

        partition ~> left
        partition ~> right

        new FanOutShape2[Either[A, B], A, B](partition.in, left.out, right.out)
      }
  }

  type Cmmt[T] = (T, String)

  implicit class CmmtOp[T](ct: Cmmt[T]) {
    def map[S](f: T => S): Cmmt[S] = (f(ct._1), ct._2)
  }

  implicit def lift[A, B, E](f: A => Either[E, B]): Cmmt[A] => Either[Cmmt[E], Cmmt[B]] = ca =>
    ca.map(f) match {
      case (Left(e), o) => Left((e, o))
      case (Right(b), o) => Right((b, o))
    }

  type E = Exception

  private def eitherShape[A, E, B](g1: Graph[FlowShape[A, Either[E, B]], Any], eSink: Sink[E, Any]) =
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val start = builder.add(g1)
      val partition = builder.add(PartitionEither[E, B]())

      start.out ~> partition.in
      partition.out0 ~> eSink

      FlowShape(start.in, partition.out1)
    }

  def partitionEither[A, B, E](f: A => Either[E, B], failureSink: Sink[E, _]) = GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._
    val start = b.add(Flow[A].map(f))
    val partition = b.add(PartitionEither[E, B]())
    start.out ~> partition.in
    partition.out0 ~> failureSink

    FlowShape(start.in, partition.out1)
  }

  def even(a: Int): Either[Exception, Int] = if (a % 2 == 0) Right(a) else Left(new IllegalArgumentException(s"$a is not acceptable"))

  val failureSink: Sink[Cmmt[E], _] =
    Flow[Cmmt[E]]
      .map(ce => ce.map(e => e.toString + "\n"))
      .map(ces => ces.map(es => ByteString(es)))
      .alsoToMat(commitSink)(Keep.none)
      .toMat(fileSink)(Keep.both)

  def fileSink: Sink[Cmmt[ByteString], _] =
    Flow[Cmmt[ByteString]]
      .map(_._1)
      .toMat(FileIO.toPath(Paths.get("/Users/barry/Workspaces/external/akka-quickstart-scala/failures.log")))(Keep.both)

  def commitSink[A]: Sink[Cmmt[A], _] =
    Flow[Cmmt[A]]
      .map(_._2)
      //      .map(_.commitScaladsl())
      .to(Sink.foreach(s => println(s"committed for ${s}")))

  Source(1 to 100)
    .map(i => (i, s"the ${i}"))
    .via(partitionEither[Cmmt[Int], Cmmt[Int], Cmmt[E]](lift(even), failureSink))
    .runForeach(println)


}
