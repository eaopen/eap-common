package org.openea.eap.extj.model.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AppFlowFormModel {
    @Schema(name = "主键id")
    private String id;
    @Schema(name = "流程名称")
    private String fullName;
    @Schema(name = "流程分类")
    private String category;
    @Schema(name = "图标")
    private String icon;
    @Schema(name = "编码")
    private String enCode;
    @Schema(name = "图标背景色")
    private String iconBackground;
    @Schema(name = "表单类型")
    private Integer formType;
    @Schema(name = "是否常用")
    private Boolean isData;

}
