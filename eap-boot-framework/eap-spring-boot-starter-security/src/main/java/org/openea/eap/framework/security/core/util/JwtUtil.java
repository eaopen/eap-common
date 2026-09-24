package org.openea.eap.framework.security.core.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
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
        return generateJwtToken(account, audience, accessTokenValiditySeconds, getSecurityProperties());
    }

    /**
     * 判断令牌是否包含 JWT ID。用于在兼容期区分新增随机 jti 的令牌与历史令牌。
     *
     * @param token JWT 或历史不透明令牌
     * @return 是否包含非空 jti
     */
    public static boolean hasJwtId(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            Object jwtId = JWTUtil.parseToken(token).getPayload("jti");
            return jwtId != null && StringUtils.isNotBlank(jwtId.toString());
        } catch (RuntimeException ignored) {
            // 历史版本允许 UUID 等不透明令牌；无法解析时按旧令牌处理。
            return false;
        }
    }

    /**
     * 判断是否为可解析但不包含 JWT ID 的历史 JWT。
     * UUID 等不透明令牌返回 {@code false}，避免破坏非 JWT 模式的缓存性能。
     *
     * @param token JWT 或历史不透明令牌
     * @return 是否为缺少 jti 的 JWT
     */
    public static boolean isJwtWithoutId(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            Object jwtId = JWTUtil.parseToken(token).getPayload("jti");
            return jwtId == null || StringUtils.isBlank(jwtId.toString());
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    static String generateJwtToken(String account, String audience, int accessTokenValiditySeconds,
                                   SecurityProperties securityProperties) {
        String jwtToken = null;
        if(securityProperties.getJwtIssuer()!=null && securityProperties.getJwtOldKey()!=null){
            if(Collections.asSet(securityProperties.getJwtOldKey().toLowerCase().split(",")).contains(securityProperties.getJwtIssuer().toLowerCase())){
                jwtToken = createOldJwtToken(account, audience, accessTokenValiditySeconds, securityProperties);
            }
        }
        if(StringUtils.isEmpty(jwtToken)){
            Map<String, Object> payload = new HashMap<>();
            // JWT 的签名结果必须在每次签发时都唯一。不能只依赖秒级 exp/iat，
            // 否则同一秒内相同 subject/audience 会生成完全相同的 token。
            payload.put("jti", IdUtil.fastSimpleUUID());
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
                    // JWT 唯一标识，兼容旧消费者（未知 claim 会被忽略）
                    .setId(IdUtil.fastSimpleUUID())
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
