package jp.classmethod.aws.jobs

import jp.classmethod.aws.jobs.ec2.CreateSnapshot
import org.junit.Test

/**
 * Created by watanabeshuji on 2015/06/11.
 */
class AWSJobFactoryTest {

    @Test
    public void "ec2 create-snapshot の場合、CreateSnapshotインスタンス"() {
        def AWSJobsConfig conf = new AWSJobsConfig()
        conf.service = "ec2"
        conf.function = "create-snapshot"
        def actual = AWSJobFactory.create(conf)
        assert actual.class == CreateSnapshot.class
    }
}
