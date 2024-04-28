package org.openea.eap.extj.model.visualJson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
@Schema(name="表单字段")
public class FormDataField {
    @Schema(name = "key")
    private String vModel;
    @Schema(name = "名称")
    private String label;
}
