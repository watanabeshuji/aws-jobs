package jp.classmethod.aws.jobs

import jp.classmethod.aws.jobs.ec2.CreateSnapshot

/**
 * Created by watanabeshuji on 2015/06/08.
 */
class AWSJobFactory {

    def static create(AWSJobsConfig conf) {
        switch (conf.service) {
            case 'ec2':
                switch (conf.function) {
                    case 'create-snapshot':
                        return new CreateSnapshot()
                    default:
                        throw new Error("no such function:" + conf.function)
                }
            default:
                throw new Error("no such service:" + conf.service)
        }
    }
}
