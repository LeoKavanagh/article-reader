package controllers

import com.micronautics.aws.Polly
import org.apache.commons.io.{IOUtils, FileUtils}
import scala.reflect.io.File
import scala.collection.JavaConverters._
import org.jsoup.Jsoup
import sys.process._

import com.amazonaws.services.polly.{AmazonPollyAsync, AmazonPollyAsyncClientBuilder}




object ArticleReader {

    object MyPolly {
      def apply: MyPolly = new MyPolly
    }

      class MyPolly extends com.micronautics.aws.Polly {
        override implicit val pollyClient: AmazonPollyAsync = AmazonPollyAsyncClientBuilder.standard.build

        /** Obtain MP3 stream from AWS Polly that voices the message */
        override def speechStream(message: String): java.io.InputStream = {
          import com.amazonaws.services.polly.model._

          val request = new SynthesizeSpeechRequest
          request.setVoiceId(VoiceId.Brian)
          request.setOutputFormat(OutputFormat.Mp3)
          request.setText(message)
          val synthesizeSpeechResult: SynthesizeSpeechResult = pollyClient.synthesizeSpeech(request)
          synthesizeSpeechResult.getAudioStream}
      }


    def get_article(url: String): String = {

            val html = Jsoup.connect(url).get()
            // all the text from the <p> elements
            val text = html.select("p").asScala.map{_.text}.filter{_ != ""}.mkString

            text
      }


    def extract_sentences(text: String): List[String] = {
            /*
            Amazon Polly can take 1500 characters at a time.
            We'll very naively assume that sentences
            (defined by a full-stop followed by a space) are shorter than this.

            Return a list of sentences
            */
            // very basic: just find a full stop followed by a space
            val sentences = text.split("\\. ").toList

            sentences
    }

    def chunk(xs: List[String]): List[String] = xs match {

          case Nil => Nil

          case y :: Nil => {
            if (y.length < 1500) List(y)
            else {
              val y_array = y.split(" ")

              val split_point: Int = y_array.length/2

              val first_half: String = y_array
                .slice(0, split_point)
                .reduce(_ + " " + _)

              val second_half: String = y_array
                .takeRight(split_point)
                .reduce(_ + " " + _)

              List(first_half, second_half)
            }
          }

          case y :: ys => {
                val candidate = y + ". " + ys.head

                if (candidate.length < 1500) chunk(candidate :: ys.tail)
                else y :: chunk(ys)
          }
    }

    def process(url: String): String = {
      val polly = new MyPolly()
      println("polly created")

      println("You passed in " + url + " as the argument")

      val text = get_article(url)
      println("got article")

      val sentences = extract_sentences(text)
      println("extracted sentences")

      val res = chunk(sentences)
      println("chunked sentences")

      val streams = res.par.map(x => polly.speechStream(x))

      val single_stream = streams.reduceLeft(new java.io.SequenceInputStream(_, _))
      println("made single stream of audio from sentence chunks")

      val file_loc = System.getProperty("user.dir") + "/public/images/"
      val ending = "article.mp3"

      val file_name: String = file_loc + ending
      var out_file: java.io.File = new java.io.File(file_name)

      java.nio.file.Files.copy(
            single_stream,
            out_file.toPath(),
            java.nio.file.StandardCopyOption.REPLACE_EXISTING)

      IOUtils.closeQuietly(single_stream)  
      println("Wrote mp3 file")
      "Wrote mp3 file"
    }

}
