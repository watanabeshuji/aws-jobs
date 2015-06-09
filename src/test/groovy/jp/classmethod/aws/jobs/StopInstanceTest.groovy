package jp.classmethod.aws.jobs

import org.junit.Ignore
import org.junit.Test

/**
 * Created by watanabeshuji on 2015/04/07.
 */
class StopInstanceTest {

    @Ignore
    @Test
    def void test() {
        def scriptText = '''
import static jp.classmethod.aws.jobs.JenkinsJobs.*

stopInstance instanceId: "i-6f46079c"
//stopInstance instanceId: "i-6f46079c", accessKey: "XXX", secretKey: "XXX"

'''
        new GroovyShell().parse(scriptText).run()
    }
}
