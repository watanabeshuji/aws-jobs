package jp.classmethod.aws.jenkins

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.PropertiesCredentials

/**
 * Created by watanabeshuji on 2015/04/07.
 */
class JobConfig {

    def String accessKey
    def String secretKey
    def String region = "ap-northeast-1"
    def int retryCount = 3

    def AWSCredentials newAWSCredentials() {
        println accessKey
        println secretKey
        if (accessKey == null || secretKey == null) return null
        new BasicAWSCredentials(accessKey, secretKey)
    }
}
