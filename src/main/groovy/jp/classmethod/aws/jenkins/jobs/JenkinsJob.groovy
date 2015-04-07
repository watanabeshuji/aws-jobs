package jp.classmethod.aws.jenkins.jobs

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.internal.StaticCredentialsProvider
import jp.classmethod.aws.jenkins.JobConfig

/**
 * Created by watanabeshuji on 2015/04/07.
 */
abstract class JenkinsJob {

    def JobConfig config = new JobConfig()

    def start() {
        preStart()
        doStart()
        postStart()
    }

    def preStart() {
    }

    def doStart() {
    }

    def postStart() {
    }

    def withCredentials(String accessKey, String secretKey) {
        config.accessKey = accessKey
        config.secretKey = secretKey
        this
    }

    def AWSCredentialsProvider getCredentialsProvider() {
        println "getCredentialsProvider"
        if (credentials != null) {
            println credentials
            new StaticCredentialsProvider(credentials)
        } else {
            println "credentials null"
            null
        }
    }

    def AWSCredentials getCredentials() {
        config.newAWSCredentials()
    }

    def String getRegion() {
        config.region
    }

    def int getRetryCount() {
        config.retryCount
    }
}
