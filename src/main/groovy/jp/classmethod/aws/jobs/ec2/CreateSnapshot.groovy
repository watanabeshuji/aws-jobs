package jp.classmethod.aws.jobs.ec2

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.CreateSnapshotRequest

/**
 * Created by watanabeshuji on 2015/06/08.
 */
class CreateSnapshot extends EC2Job {

    def execute() {
        def req = new CreateSnapshotRequest().withVolumeId(volumeId)
        if (config.hasParam("description")) req.setDescription(config.getParam("description"))
        client.createSnapshot(req)
    }

}
