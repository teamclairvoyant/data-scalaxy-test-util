package com.clairvoyant.data.scalaxy.test.util.mock

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.clairvoyant.data.scalaxy.test.util.SparkUtil
import com.clairvoyant.data.scalaxy.test.util.mock.S3BucketMock.*
import io.findify.s3mock.S3Mock
import org.apache.hadoop.conf.Configuration
import org.scalatest.{BeforeAndAfterAll, Suite}

object S3BucketMock {
  val s3MockAWSAccessKey = "test_access_key"
  val s3MockAWSSecretKey = "test_secret_key"
  val s3MockPort: Int = 8082
  val s3MockEndpoint: String = s"http://localhost:$s3MockPort"
}

trait S3BucketMock extends SparkUtil with BeforeAndAfterAll {
  this: Suite =>

  val hadoopConfigurations: Configuration = sparkSession.sparkContext.hadoopConfiguration

  hadoopConfigurations.set("fs.s3a.endpoint", s3MockEndpoint)
  hadoopConfigurations.set("fs.s3a.access.key", s3MockAWSAccessKey)
  hadoopConfigurations.set("fs.s3a.secret.key", s3MockAWSSecretKey)
  hadoopConfigurations.set("fs.s3a.path.style.access", "true")
  hadoopConfigurations.set("fs.s3a.change.detection.version.required", "false")

  lazy val s3Client: AmazonS3 =
    AmazonS3ClientBuilder
      .standard()
      .disableChunkedEncoding
      .withPathStyleAccessEnabled(true)
      .withEndpointConfiguration(new EndpointConfiguration(s3MockEndpoint, Regions.US_EAST_1.getName))
      .withCredentials(
        new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3MockAWSAccessKey, s3MockAWSSecretKey))
      )
      .build

  val s3Mock: S3Mock = S3Mock(port = s3MockPort)

  override def beforeAll(): Unit = s3Mock.start

  override def afterAll(): Unit = s3Mock.shutdown
}
