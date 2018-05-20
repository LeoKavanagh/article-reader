import scala.util.{Success, Failure, Try}
import scala.io.Source
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import org.jsoup.Jsoup
import com.micronautics.aws.Polly

object ProcessText {

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

    case y :: Nil => List(y)

    case y :: ys => {
      val candidate = y + ". " + ys.head

      if (candidate.length < 1500) chunk(candidate :: ys.tail)
      else y :: chunk(ys)
    }
  }
}
