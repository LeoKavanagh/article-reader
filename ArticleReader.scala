import scala.util.{Success, Failure, Try}
import scala.io.Source
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import org.jsoup.Jsoup
import com.micronautics.aws.Polly

import ProcessText.{get_article, extract_sentences, chunk}
import GenerateAudio.make_mp3

object ArticleReader {

    def main(args: Array[String]): Unit = {

      val url = args(0)
      println("You passed in " + url + " as the argument")

      val polly = new Polly()
      val file_loc = "/home/leo/repos/article_reader/article_reader/tmp"
      val ending = ".mp3"

      val text = get_article(url)
      val sentences = extract_sentences(text)
      val res = chunk(sentences)

      for((text, i) <- res.zipWithIndex){
        make_mp3(text, i, polly, file_loc, ending)
      }

      println("Now in the terminal run 'cat tmp*.mp3 >> article.mp3' to put it all together")



  }

}
