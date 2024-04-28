package org.openea.eap.extj.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class FileListVO {
    @Schema(name = "主键id")
    private String fileId;
    @Schema(name = "文件名称")
    private String fileName;
    @Schema(name = "文件大小")
    private String fileSize;
    @Schema(name = "修改时间")
    private String fileTime;
    @Schema(name = "文件类型")
    private String fileType;
}
