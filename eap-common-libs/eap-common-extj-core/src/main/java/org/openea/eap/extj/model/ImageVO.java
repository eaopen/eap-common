package org.openea.eap.extj.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 *
 */
@Data
public class ImageVO {
    @Schema(name = "路径")
    private String domain;
    @Schema(name = "链接")
    private String link;
    @Schema(name = "名称")
    private String name;
    @Schema(name = "名称")
    private String originalName;
}
