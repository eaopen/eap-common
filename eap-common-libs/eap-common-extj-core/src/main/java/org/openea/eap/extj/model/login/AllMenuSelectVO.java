package org.openea.eap.extj.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AllMenuSelectVO {
    @Schema(name = "主键")
    private String id;
    @Schema(name = "名称")
    private String fullName;
    @Schema(name = "菜单编码")
    private String enCode;
    @Schema(name = "父主键")
    private String parentId;
    @Schema(name = "图标")
    private String icon;
    @Schema(name = "是否有下级菜单")
    private Boolean hasChildren;
    @Schema(name = "菜单地址")
    private String urlAddress;
    @Schema(name = "链接目标")
    private String linkTarget;
    @Schema(name = "下级菜单列表")
    private List<AllMenuSelectVO> children;
    @Schema(name = "菜单分类【1-类别、2-页面】")
    private Integer type;
    private String propertyJson;
    private Long sortCode;

    private String systemId;
}
