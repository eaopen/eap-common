package org.openea.eap.extj.model.visualJson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
@Schema(name = "表字段模型")
public class TableFields {
    @Schema(name = "字段名")
    private String field;
    @Schema(name = "字段备注")
    private String comment;
    @Schema(name = "类型")
    private String dataType;
    @Schema(name = "是否主键")
    private Integer isPrimaryKey;
}
