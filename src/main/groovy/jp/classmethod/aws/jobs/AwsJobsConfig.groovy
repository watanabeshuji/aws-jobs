package jp.classmethod.aws.jobs

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import com.amazonaws.services.securitytoken.model.Credentials
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
    def String roleArn
    def String accessKey
    def String secretKey
    def String region = "ap-northeast-1"
    def Map<String, String> params = [:]
    def int retryCount = 3

    def AWSCredentials newAWSCredentials() {
        if (useAccessKey()) {
            new BasicAWSCredentials(accessKey, secretKey)
        } else if (useAssumeRole()) {
            newAssumeRoleCredentials()
        } else {
            null
        }
    }

    private useAccessKey() {
        accessKey != null && secretKey != null
    }

    private useAssumeRole() {
        roleArn != null
    }

    private AWSCredentials newAssumeRoleCredentials() {
        def client = Region.getRegion(Regions.fromName(region)).createClient(AWSSecurityTokenServiceClient, null, null)
        def req = new AssumeRoleRequest()
            .withRoleArn(roleArn)
            .withDurationSeconds(3600)
            .withRoleSessionName("job.classmethod.net")
        def res = client.assumeRole(req)
        new BasicSessionCredentials(res.getCredentials().accessKeyId, res.getCredentials().secretAccessKey, res.getCredentials().sessionToken)
    }


    def hasParam(String key) {
        this.params.containsKey(key)
    }

    def getParam(String key) {
        this.params.get(key)
    }

    def getIntParam(String key) {
        Integer.parseInt(this.params.get(key))
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
                    case '--role_arn':
                        AWSJobsConfig.instance.roleArn = args[++i]
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
