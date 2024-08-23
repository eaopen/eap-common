package org.openea.eap.framework.security.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;

public class PocAuthUtil {
    public static String authSignToday(String user, String signPasswd){
        // sign=md5(user+today+signPasswd)
        String sign = DigestUtil.md5Hex(user + DateUtil.today() + signPasswd);
        return sign;
    }
}
