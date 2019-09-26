package net.sansa_stack.examples.spark.rdf

import scala.collection.mutable

import net.sansa_stack.rdf.spark.io._
import net.sansa_stack.rdf.spark.model.graph._
import org.apache.jena.riot.Lang
import org.apache.spark.graphx.Graph
import org.apache.spark.sql.SparkSession

/*
 * Computes the PageRank of Resources from an input .nt file.
 */
object PageRank {

  def main(args: Array[String]) {
    parser.parse(args, Config()) match {
      case Some(config) =>
        run(config.in)
      case None =>
        println(parser.usage)
    }
  }
  def run(input: String): Unit = {

    println("======================================")
    println("|   PageRank of resources example    |")
    println("======================================")

    val spark = SparkSession.builder
      .appName(s"PageRank of resources example ( $input )")
      .master("local[*]")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      // .config("spark.kryo.registrator", "net.sansa_stack.rdf.spark.io.JenaKryoRegistrator")
      .getOrCreate()

    val lang = Lang.NTRIPLES
    val triples = spark.rdf(lang)(input)



    spark.stop

  }
  case class Config(in: String = "")

  // the CLI parser
  val parser = new scopt.OptionParser[Config]("PageRank of resources example") {

    head(" PageRank of resources example")

    opt[String]('i', "input").required().valueName("<path>").
      action((x, c) => c.copy(in = x)).
      text("path to file that contains the data (in N-Triples format)")
    help("help").text("prints this usage text")
  }
}
