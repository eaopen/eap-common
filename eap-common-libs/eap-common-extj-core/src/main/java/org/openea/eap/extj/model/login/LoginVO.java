package org.openea.eap.extj.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class LoginVO {
    @Schema(name = "token")
    private String token;
    @Schema(name = "主题")
    private String theme;
}
