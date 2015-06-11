package jp.classmethod.aws.jobs.utils

import java.text.SimpleDateFormat

/**
 * Created by watanabeshuji on 2015/06/11.
 */
class FormatUtils {

    static String format(String format) {
        if (0 <= format.indexOf('{datetime}')) {
            format = format.replace('{datetime}', new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()))
        }
        format
    }
}
