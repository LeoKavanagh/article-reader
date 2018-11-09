
import com.micronautics.aws.Polly
import org.apache.commons.io.{IOUtils, FileUtils}
import scala.reflect.io.File
import scala.collection.JavaConverters._
import org.jsoup.Jsoup


object ArticleReader extends App{


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

      def make_mp3(text: String, idx: Int, polly: com.micronautics.aws.Polly,
                   file_loc: String, ending: String): Unit = {

        val sound_stream: java.io.InputStream = polly.speechStream(text)

        val file_name: String = file_loc + idx.toString + ending
        var out_file: java.io.File = new java.io.File(file_name)

        java.nio.file.Files.copy(
              sound_stream,
              out_file.toPath(),
              java.nio.file.StandardCopyOption.REPLACE_EXISTING)

        IOUtils.closeQuietly(sound_stream)

      }


    val url = args(0)
    println("You passed in " + url + " as the argument")

    val polly = new Polly()
    println("polly created")

    // val file_loc = "/home/leo/repos/article-reader/tmp"
    val file_loc = System.getProperty("user.dir") + "/"
    val ending = "tmp.mp3"

    val text = get_article(url)
    println("got article")

    val sentences = extract_sentences(text)
    println("extracted sentences")

    val res = chunk(sentences)
    println("chunked sentences")


    for((text, i) <- res.zipWithIndex){
      make_mp3(text, i, polly, file_loc, ending)
    }

}
