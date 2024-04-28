package org.openea.eap.extj.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class UserSystemVO {
    @Schema(name = "系统id")
    private String id;
    @Schema(name = "系统名称")
    private String name;
    @Schema(name = "系统图标")
    private String icon;
    @Schema(name = "是否当前系统")
    private Boolean currentSystem = false;
}
