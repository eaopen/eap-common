package org.openea.eap.framework.security.core.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.internal.util.Collections;
import org.openea.eap.framework.common.util.spring.SpringUtils;
import org.openea.eap.framework.security.config.SecurityProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    private static SecurityProperties _SecurityProperties;
    private static SecurityProperties getSecurityProperties(){
        if(_SecurityProperties==null){
            _SecurityProperties = SpringUtils.getBean(SecurityProperties.class);
        }
        return _SecurityProperties;
    }
    public static String  generateJwtToken(String account, String audience, int accessTokenValiditySeconds){
        SecurityProperties securityProperties = getSecurityProperties();
        String jwtToken = null;
        if(securityProperties.getJwtIssuer()!=null && securityProperties.getJwtOldKey()!=null){
            if(Collections.asSet(securityProperties.getJwtOldKey().toLowerCase().split(",")).contains(securityProperties.getJwtIssuer().toLowerCase())){
                jwtToken = createOldJwtToken(account, audience, accessTokenValiditySeconds, securityProperties);
            }
        }
        if(StringUtils.isEmpty(jwtToken)){
            Map<String, Object> payload = new HashMap<>();
            payload.put("username", account);
            if(StringUtils.isNotEmpty(audience)){
                payload.put("audience", audience);
            }
            if(StringUtils.isNotEmpty(securityProperties.getJwtIssuer())){
                payload.put("iss", securityProperties.getJwtIssuer());
            }
            if(accessTokenValiditySeconds>0){
                payload.put("exp", DateUtil.offsetSecond(new Date(), accessTokenValiditySeconds));
            }
            jwtToken = JWTUtil.createToken(payload, securityProperties.getJwtSecret().getBytes());
        }
        return jwtToken;
    }

    public static String createOldJwtToken(String account, String audience, int accessTokenValiditySeconds, SecurityProperties jwtPros) {
        String token = null;
        try{
            // check default secret
            String jwtSecret = jwtPros.getJwtSecret();
            if("eapJwt2000".equals(jwtSecret)){
                jwtSecret = "asd%WE^@&fas156dfa";
            }
            if(accessTokenValiditySeconds<=0){
                accessTokenValiditySeconds = 1800; // 30 min
            }
            JwtBuilder builder = Jwts.builder()
                    // jwt签发者
                    .setIssuer(jwtPros.getJwtIssuer())
                    // jwt所面向的用户
                    .setSubject(account)
                    // 接收jwt的一方
                    .setAudience(audience)
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS512, Encoders.BASE64.encode(jwtSecret.getBytes()) )
                    .setExpiration(DateUtil.offset(new Date(), DateField.SECOND, accessTokenValiditySeconds));
            token = builder.compact();
        } catch (Exception e) {
            log.error("createOldJwtToken error: {}", e.getMessage());
        }
        return token;
    }

}
