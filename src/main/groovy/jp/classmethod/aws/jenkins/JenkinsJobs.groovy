package jp.classmethod.aws.jenkins

import jp.classmethod.aws.jenkins.jobs.StopInstance

/**
 * Created by watanabeshuji on 2015/04/06.
 */
class JenkinsJobs {

    static def stopInstance(Map params) {
        println "start StopInstance $params['instanceId']"
        new StopInstance()
            .withInstanceId(params['instanceId'])
            .withCredentials(params['accessKey'], params['secretKey'])
            .start()
    }

}
