import scala.util.{Success, Failure, Try}
import scala.io.Source
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.XML
import scala.collection.JavaConverters._
import org.jsoup.Jsoup
import sys.process.Process
import com.micronautics.aws.Polly

object GetText {

  val url = "https://www.irishtimes.com/business/economy/time-to-tax-narcotics-as-war-on-drugs-will-never-be-won-1.3493582"

  val html = Jsoup.connect(url).get()

  // all the text from the <p> elements
  val text = html.select("p").asScala.map{_.text}.filter{_ != ""}.mkString


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

  val sentences = extract_sentences(text)

  // val sentences = List("riverrun past eve and adams",
  // "it was the best of times it was the worst of times",
  // "a bright cold day in april",
  // " jfc he is driving me insane")
  // val sentences = List("abcd", "ab", "abc", "a", "a", "abcde", "ab")

  sentences.map(_.length)

  def chunk(xs: List[String]): List[String] = xs match {

  case Nil => Nil

  case y :: Nil => List(y)

  case y :: ys => {
          val candidate = y + ". " + ys.head

          if (candidate.length < 1500) chunk(candidate :: ys.tail)
          else y :: chunk(ys)
    }
  }

  val res = chunk(sentences)

  res.map(_.length)
  // res

  // use awslib_scala, which is a wrapper around the java library
  val polly = new Polly()
  var out_file = new java.io.File("/home/leo/repos/article_reader/article_reader/test.mp3")
 
  // val aws_base = """aws polly synthesize-speech --output-format mp3 --voice-id Brian --text '"""
  // val ending = ".mp3"
  // val cmds = (0 until res.length).map(x => aws_base + res(x) + outfile + x.toString() + ending) 
  
  for(text_chunk <- res){
	val sound_stream = polly.speechStream(text_chunk)

    java.nio.file.Files.copy(
      sound, 
      out_file.toPath(), 
      java.nio.file.StandardCopyOption.REPLACE_EXISTING);
  }

  org.apache.commons.io.IOUtils.closeQuietly(sound);
}
