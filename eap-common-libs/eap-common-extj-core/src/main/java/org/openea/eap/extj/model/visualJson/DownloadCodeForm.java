package org.openea.eap.extj.model.visualJson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
@Schema(name="下载代码表单")
public class DownloadCodeForm {
    /**
     * 所属模块
     */
    @Schema(name = "所属模块")
    private String module;
    /**
     * 主功能名称
     */
    @Schema(name = "主功能名称")
    private String className;
    /**
     * 子表名称集合
     */
    @Schema(name = "子表名称集合")
    private String subClassName;
    /**
     * 主功能备注
     */
    @Schema(name = "主功能备注")
    private String description;

    /**
     * 数据源id
     */
    @Schema(name = "数据源id")
    private String dataSourceId;

}
