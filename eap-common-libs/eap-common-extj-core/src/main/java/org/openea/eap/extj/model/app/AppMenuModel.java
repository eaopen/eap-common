package org.openea.eap.extj.model.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AppMenuModel {
    @Schema(name = "扩展字段")
    private String propertyJson;
    @Schema(name = "菜单编码")
    private String enCode;
    @Schema(name = "菜单名称")
    private String fullName;
    @Schema(name = "图标")
    private String icon;
    @Schema(name = "主键id")
    private String id;
    @Schema(name = "链接地址")
    private String urlAddress;

}
