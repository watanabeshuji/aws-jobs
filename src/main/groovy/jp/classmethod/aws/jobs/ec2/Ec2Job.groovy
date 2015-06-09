package jp.classmethod.aws.jobs.ec2

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.DescribeInstancesResult
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation
import jp.classmethod.aws.jobs.AWSJobsConfig

/**
 * Created by watanabeshuji on 2015/06/08.
 */
class EC2Job {

    def AmazonEC2Client client

    def AmazonEC2Client getClient() {
        if (!client) {
            client = Region.getRegion(Regions.fromName(region))
                .createClient(AmazonEC2Client, credentialsProvider, null)
        }
        client
    }

    def AWSCredentialsProvider getCredentialsProvider() {
        println "getCredentialsProvider"
        def credentials = getCredentials()
        if (credentials != null) {
            println credentials
            new StaticCredentialsProvider(credentials)
        } else {
            println "credentials null"
            null
        }
    }

    def getVolumeId() {
        if (config.hasParam("volume_id")) {
            return config.getParam("volume_id")
        } else {
            def instanceId = config.getParam("instance_id")
            def req = new DescribeInstancesRequest().withInstanceIds(instanceId)
            return getFirstEbsVolumeId(getClient().describeInstances(req).getReservations())
        }
    }

    def String getFirstEbsVolumeId(List<Reservation> reservations) {
        for (Reservation r: reservations) {
            for (Instance i: r.getInstances()) {
                return i.getBlockDeviceMappings().get(0).getEbs().getVolumeId()
            }
        }
        throw new AssertionError()
    }


    def AWSJobsConfig getConfig() {
        AWSJobsConfig.get()
    }

    def AWSCredentials getCredentials() {
        config.newAWSCredentials()
    }

    def String getRegion() {
        config.region
    }

}
