import akka.stream._
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicLong

import MainApp.system
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.Directives
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.kafka.{ConsumerSettings, ProducerMessage, ProducerSettings, Subscriptions}
import akka.kafka.scaladsl.{Committer, Consumer, Producer}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer, StringSerializer}
import org.apache.kafka.common.serialization.{Deserializer, Serializer, StringDeserializer, StringSerializer}

import scala.io.StdIn
import scala.util.Random
import scala.util.{Failure, Success}

object MainApp extends App{
  implicit val system: ActorSystem = ActorSystem("MySystem")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  var seq_3: Seq[Int] = Seq.empty
  val config = system.settings.config.getConfig("akka.kafka.producer")
  val server = system.settings.config.getString("akka.kafka.producer.kafka-clients.server")
  val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
      .withBootstrapServers(server)

  def f7():Unit = {
    var mySeq: Seq[Int] = Seq.empty
    for (_ <- 0 to 10){
      mySeq :+= Random.nextInt(30)
    }

    val done2: Future[Done] =
      Source(mySeq)
        .map(_.toString)
        .map { value =>
          ProducerMessage.multi(
            Seq(
              new ProducerRecord[String, String]("topic_3", "key_1", value),
              new ProducerRecord[String, String]("topic_4", "key_2", value)
            )
          )
        }
        .via(Producer.flexiFlow(producerSettings))
        .run()

    done2.onComplete {
      case Success(value) => {
        println(value)
        system.terminate()
      }
      case Failure(exception) => {
        println(exception)
        system.terminate()
      }
    }
  }

  Thread.sleep(5000)
  def f8(implicit num_param: Int = 0): Unit = {
    class OffsetStore {
      private val offset = new AtomicLong
      def businessLogicAndStoreOffset(record: ConsumerRecord[String, String]): Future[Done] = {
        print(s"DB.save: ${record.value}")
        seq_3 :+= record.value.toInt
        Future.successful(Done)
      }
      def businessLogicAndStoreOffset_2(record: ConsumerRecord[String, String]): Future[Done] = {
        f10(record.value)
        system.terminate()
        Future.successful(Done)
      }
      def loadOffset(): Future[Long] = {
        Future.successful(offset.get)
      }
    }
    val bootstrapServers: String = system.settings.config.getString("akka.kafka.producer.kafka-clients.server")

    val consumerSettings: ConsumerSettings[String, String] =
      ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServers)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
      .withClientId("externalOffsetStorage")

    if (num_param == 0) {
      val db = new OffsetStore
      db.loadOffset().map { fromOffset =>
        Consumer
          .plainSource(
            consumerSettings,
            Subscriptions.assignmentWithOffset(
              new TopicPartition("topic_3", 0) -> fromOffset,
              new TopicPartition("topic_4", 0) -> fromOffset
            )
          )
          .mapAsync(1)(db.businessLogicAndStoreOffset)
          .toMat(Sink.seq)(DrainingControl.apply)
          .run()
      }
    }
    else {
      val db = new OffsetStore
      db.loadOffset().map { fromOffset =>
        Consumer
          .plainSource(
            consumerSettings,
            Subscriptions.assignmentWithOffset(
              new TopicPartition("topic_6", 0) -> fromOffset,
            )
          )
          .mapAsync(1)(db.businessLogicAndStoreOffset_2)
          .toMat(Sink.seq)(DrainingControl.apply)
          .run()
      }
    }
    println("finished")
  }
  f8()
  Thread.sleep(10000)

  def f9(): Unit = {
    val num: Int = seq_3.max
    val done: Future[Done] =
      Source.single(num)
        .map(_.toString)
        .map(value => new ProducerRecord[String, String]("topic_6", value))
        .runWith(Producer.plainSink(producerSettings))

    Thread.sleep(5000)
    done.onComplete {
      case Success(_) =>
        f8(num)
      case Failure(_) => println("bad")
    }
  }
  f9()

  def f10(str: String) : Unit = {
    HttpServer.callHttp(str)
  }
}

