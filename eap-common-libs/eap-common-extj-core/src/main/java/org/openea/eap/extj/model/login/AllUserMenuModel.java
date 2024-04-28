package org.openea.eap.extj.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import org.openea.eap.extj.util.treeutil.SumTree;
import lombok.Data;

@Data
public class AllUserMenuModel extends SumTree {
    @Schema(name = "名称")
    private String fullName;
    @Schema(name = "菜单编码")
    private String enCode;
    @Schema(name = "图标")
    private String icon;
    @Schema(name = "菜单地址")
    private String urlAddress;
    @Schema(name = "链接目标")
    private String linkTarget;
    @Schema(name = "菜单分类【1-类别、2-页面】")
    private Integer type;
    private String propertyJson;
    private Long sortCode;

    private String systemId;
}
