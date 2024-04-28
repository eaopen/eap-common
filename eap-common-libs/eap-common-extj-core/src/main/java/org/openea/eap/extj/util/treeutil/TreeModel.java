package org.openea.eap.extj.util.treeutil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要实现树的类可以继承该类，手写set方法，在设定本身属性值时同时设置该类中的相关属性
 *
 */
@Data
public class TreeModel<T> {
    @Schema(name = "主键")
    private String id;
    @Schema(name = "名称")
    private String fullName;
    @Schema(name = "父主键")
    private String parentId;
    @Schema(name = "是否有下级菜单")
    private Boolean hasChildren = true;
    @Schema(name = "图标")
    private String icon;
    @Schema(name = "下级菜单列表")
    private List<TreeModel<T>> children = new ArrayList<>();
}