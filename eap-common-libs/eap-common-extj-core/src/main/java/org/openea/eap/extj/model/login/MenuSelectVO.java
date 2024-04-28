package org.openea.eap.extj.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 *
 *
 */
@Data
public class MenuSelectVO {
    @Schema(name = "主键")
    private String id;
    @Schema(name = "父主键")
    private String parentId;
    @Schema(name = "名称")
    private String fullName;
    @Schema(name = "是否按钮权限")
    private Integer isButtonAuthorize;
    @Schema(name = "是否列表权限")
    private Integer isColumnAuthorize;
    @Schema(name = "是否数据权限")
    private Integer isDataAuthorize;
    @Schema(name = "排序码")
    private Long sortCode;
    @Schema(name = "图标")
    private String icon;
    @Schema(name = "是否有下级菜单")
    private Boolean hasChildren;
    @Schema(name = "下级菜单列表")
    private List<MenuSelectVO> children;

    @Schema(name = "外链")
    private String linkTarget;
    @Schema(name = "编码")
    private String enCode;


    @Schema(name = "系统id")
    private String systemId;

    @Schema(name = "分类")
    private Integer type;
}
