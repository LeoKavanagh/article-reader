import scala.collection.JavaConverters._
import org.apache.commons.io.IOUtils
import java.io.InputStream

object GenerateAudio {

  def make_mp3(text: String, idx: Int, polly: com.micronautics.aws.Polly, file_loc: String, ending: String): Unit = {

    val sound_stream: java.io.InputStream = polly.speechStream(text)

    val file_name: String = file_loc + idx.toString + ending
    var out_file: java.io.File = new java.io.File(file_name)

    java.nio.file.Files.copy(
    sound_stream,
    out_file.toPath(),
    java.nio.file.StandardCopyOption.REPLACE_EXISTING)

    IOUtils.closeQuietly(sound_stream)

  }
}
