package org.openea.eap.extj.util.treeutil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 *
 */
@Data
public class TreeViewModel {
    @Schema(name = "主键")
    private String id;
    private String code;
    @Schema(name = "名称")
    private String text;
    private String title;
    @Schema(name = "父主键")
    private String parentId;
    @Schema(name = "选中")
    private Integer checkstate;
    private Boolean showcheck = true;
    private Boolean isexpand = true;
    private Boolean complete = true;
    @Schema(name = "图标")
    private String img;
    @Schema(name = "样式")
    private String cssClass;
    @Schema(name = "是否有下级菜单")
    private Boolean hasChildren;
    @Schema(name = "其他")
    private Map<String, Object> ht;
    @Schema(name = "是否点击")
    private Boolean click;
    @Schema(name = "下级菜单列表")
    private List<TreeViewModel> childNodes;
}
