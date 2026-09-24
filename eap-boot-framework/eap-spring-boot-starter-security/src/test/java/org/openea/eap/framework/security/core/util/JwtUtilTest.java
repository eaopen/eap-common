package org.openea.eap.framework.security.core.util;

import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.openea.eap.framework.security.config.SecurityProperties;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    @Test
    void generateJwtTokenShouldBeUniqueForSameIdentity() {
        SecurityProperties properties = new SecurityProperties();
        properties.setJwtIssuer("new-issuer");
        properties.setJwtOldKey("legacy-issuer");
        properties.setJwtSecret("test-secret");

        String first = JwtUtil.generateJwtToken("user-key", "default", 1800, properties);
        String second = JwtUtil.generateJwtToken("user-key", "default", 1800, properties);

        assertThat(first).isNotBlank();
        assertThat(second).isNotBlank().isNotEqualTo(first);
        assertThat(JWTUtil.parseToken(first).getPayload("jti")).isNotNull();
        assertThat(JWTUtil.parseToken(second).getPayload("jti"))
                .isNotNull()
                .isNotEqualTo(JWTUtil.parseToken(first).getPayload("jti"));
        assertThat(JwtUtil.hasJwtId(first)).isTrue();
        assertThat(JwtUtil.isJwtWithoutId(first)).isFalse();
    }

    @Test
    void createOldJwtTokenShouldBeUniqueForSameIdentity() {
        SecurityProperties properties = new SecurityProperties();
        properties.setJwtIssuer("openBPM");
        properties.setJwtSecret("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");

        String first = JwtUtil.createOldJwtToken("user-key", "default", 1800, properties);
        String second = JwtUtil.createOldJwtToken("user-key", "default", 1800, properties);

        assertThat(first).isNotBlank();
        assertThat(second).isNotBlank().isNotEqualTo(first);
        assertThat(JWTUtil.parseToken(first).getPayload("jti")).isNotNull();
        assertThat(JWTUtil.parseToken(second).getPayload("jti"))
                .isNotNull()
                .isNotEqualTo(JWTUtil.parseToken(first).getPayload("jti"));
        assertThat(JwtUtil.hasJwtId(first)).isTrue();
        assertThat(JwtUtil.isJwtWithoutId(first)).isFalse();
    }

    @Test
    void hasJwtIdShouldTreatLegacyAndOpaqueTokensAsOldTokens() {
        String legacyJwt = JWTUtil.createToken(Map.of("username", "legacy-user"), "test-secret".getBytes());

        assertThat(JwtUtil.hasJwtId(legacyJwt)).isFalse();
        assertThat(JwtUtil.isJwtWithoutId(legacyJwt)).isTrue();
        assertThat(JwtUtil.hasJwtId("legacy-opaque-token")).isFalse();
        assertThat(JwtUtil.isJwtWithoutId("legacy-opaque-token")).isFalse();
        assertThat(JwtUtil.hasJwtId(null)).isFalse();
        assertThat(JwtUtil.isJwtWithoutId(null)).isFalse();
    }
}
