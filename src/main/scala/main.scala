
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.s3.model.{GetObjectRequest, S3Exception}

import java.io.FileOutputStream
import java.util.zip.{Deflater, ZipEntry, ZipOutputStream}

object main extends App {
  private val logger = LoggerFactory.getLogger(this.getClass)

  import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
  import software.amazon.awssdk.regions.Region
  import software.amazon.awssdk.services.s3.S3Client

  val credentialsProvider = ProfileCredentialsProvider.create
  val region = Region.EU_CENTRAL_1
  val s3 = S3Client.builder.region(region).credentialsProvider(credentialsProvider).build
  val bucketName = "mojitestbucket"
  val filesList = List("1.png", "2.png", "3.png", "4.png", "5.png");

  def testZipFiles(): Unit = {
    val zos = new ZipOutputStream(new FileOutputStream("test-download.zip"))
    zos.setMethod(ZipOutputStream.DEFLATED)
    zos.setLevel(Deflater.NO_COMPRESSION)
    filesList.foreach(f => {
      val s: ZipEntry = new ZipEntry(f)
      zos.putNextEntry(s)
      zos.write(getObjectBytes(f))
      zos.closeEntry()
    })
    //final
    zos.close()

  }

  def getObjectBytes(keyName: String): Array[Byte] = {
    try {
      val objectRequest = GetObjectRequest.builder.key(keyName).bucket(bucketName).build
      val objectBytes = s3.getObjectAsBytes(objectRequest)
      val bs: Array[Byte] = objectBytes.asByteArray;
      bs
    } catch {
      case e: S3Exception =>
        println(e.awsErrorDetails.errorMessage)
        Array[Byte]()
    }
  }

  testZipFiles
}