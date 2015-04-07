package jp.classmethod.aws.jenkins.jobs

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.StopInstancesRequest

/**
 * Created by watanabeshuji on 2015/04/06.
 */
class StopInstance extends JenkinsJob {

    def String instanceId

    def withInstanceId(String instanceId) {
        this.instanceId = instanceId
        this
    }

    def doStart() {
        def client = Region.getRegion(Regions.fromName(region)).createClient(AmazonEC2Client, credentialsProvider, null)
        def req = new StopInstancesRequest().withInstanceIds(this.instanceId)
        client.stopInstances(req)
    }
}
