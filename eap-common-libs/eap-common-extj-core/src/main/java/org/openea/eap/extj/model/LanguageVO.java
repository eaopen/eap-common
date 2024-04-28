package org.openea.eap.extj.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class LanguageVO {
    @Schema(name = "语言编码")
    private String encode;
    @Schema(name = "语言名称")
    private String fullName;
}
