package org.openea.eap.extj.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 *
 *
 */
@Data
@Builder
public class UploaderVO {
    @Schema(name = "名称")
    private String name;
    @Schema(name = "请求接口")
    private String url;
    @Schema(name = "预览文件id")
    private String fileVersionId;
    @Schema(name = "文件大小")
    private Long fileSize;
    @Schema(name = "文件后缀")
    private String fileExtension;
}
