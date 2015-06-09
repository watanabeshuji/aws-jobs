package jp.classmethod.aws.jobs

/**
 * Created by watanabeshuji on 2015/06/08.
 */
class AWSJobsMain {

    public static void main(String[] args) {
        def conf = AWSJobsConfig.load(args)
        println conf
        def job = AWSJobFactory.create(conf)
        job.execute()
    }
}
