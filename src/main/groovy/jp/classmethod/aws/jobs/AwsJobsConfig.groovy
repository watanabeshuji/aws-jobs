package jp.classmethod.aws.jobs

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2015/04/07.
 */
@Canonical
class AWSJobsConfig {

    static AWSJobsConfig instance = new AWSJobsConfig()

    def String service
    def String function
    def boolean debug = false
    def String accessKey
    def String secretKey
    def String region = "ap-northeast-1"
    def Map<String, String> params = [:]
    def int retryCount = 3

    def AWSCredentials newAWSCredentials() {
        println accessKey
        println secretKey
        if (accessKey == null || secretKey == null) return null
        new BasicAWSCredentials(accessKey, secretKey)
    }

    def hasParam(String key) {
        this.params.containsKey(key)
    }

    def getParam(String key) {
        this.params.get(key)
    }

    static AWSJobsConfig get() {
        AWSJobsConfig.instance
    }

    static AWSJobsConfig load(String[] args) {
        try {
            AWSJobsConfig.instance.service = args[0]
            AWSJobsConfig.instance.function = args[1]
            for (int i = 2; i < args.length; i++) {
                switch (args[i]) {
                    case '-debug':
                        AWSJobsConfig.instance.debug = true
                        break
                    case '--aws_access_key_id':
                        AWSJobsConfig.instance.accessKey = args[++i]
                        break
                    case '--aws_secret_access_key':
                        AWSJobsConfig.instance.secretKey = args[++i]
                        break
                    default:
                        AWSJobsConfig.instance.params[args[i].substring(2)] = args[i + 1]
                        i++
                        break
                }
            }
            AWSJobsConfig.instance
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Error("too few arguments.")
        }
    }
}
