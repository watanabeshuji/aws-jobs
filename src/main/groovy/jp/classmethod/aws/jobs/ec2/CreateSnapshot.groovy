package jp.classmethod.aws.jobs.ec2

import com.amazonaws.services.ec2.model.*
import jp.classmethod.aws.jobs.utils.FormatUtils

import java.nio.file.attribute.FileTime

/**
 * Created by watanabeshuji on 2015/06/08.
 */
class CreateSnapshot extends EC2Job {

    static final String TAG = "ec2:create-snapshot"

    def execute() {
        String volumeId = getVolumeId()
        Snapshot snapshot = createSnapShot(volumeId)
        def tags = new ArrayList<>()
        createTags(snapshot.snapshotId,
            new Tag("Name", createName()),
            new Tag("cm:aws-job", TAG))
        if (isManagementSnapshots()) {
            int max = config.getIntParam("max-snapshots")
            List<Snapshot> snapshots = getSnapshots(volumeId)
            if (max < snapshots.size()) {
                snapshots.subList(0, snapshots.size() - max).each {
                    deleteSnapshot(it.snapshotId)
                }
            }
        }
    }

    private String createName() {
        if (config.hasParam("name")) {
            FormatUtils.format(config.getParam("name"))
        } else {
            ""
        }
    }

    private Snapshot createSnapShot(String volumeId) {
        println "create snapshot ${volumeId}"
        def req = new CreateSnapshotRequest().withVolumeId(volumeId)
        if (config.hasParam("description")) req.setDescription(config.getParam("description"))
        def res = client.createSnapshot(req)
        res.snapshot
    }

    private boolean isManagementSnapshots() {
        config.hasParam("max-snapshots")
    }

    private List<Snapshot> getSnapshots(String volumeId) {
        def req = new DescribeSnapshotsRequest()
            .withFilters(
                new Filter().withName("volume-id").withValues(volumeId),
                new Filter().withName("tag:cm:aws-job").withValues(TAG)
            )
        def res = client.describeSnapshots(req)
        def snapshots = res.snapshots
        Collections.sort(snapshots, new Comparator<Snapshot>() {
            @Override
            int compare(Snapshot ss1, Snapshot ss2) {
                return ss1.startTime.compareTo(ss2.startTime)
            }
        })
        snapshots
    }

    private void createTags(String resourceId, Tag... tags) {
        def req = new CreateTagsRequest().withResources(resourceId).withTags(tags)
        client.createTags(req)
    }

    private void deleteSnapshot(String snapshotId) {
        println "delete snapshot ${snapshotId}"
        def req = new DeleteSnapshotRequest().withSnapshotId(snapshotId)
        client.deleteSnapshot(req)
    }

}
