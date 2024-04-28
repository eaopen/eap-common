package org.openea.eap.extj.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadVO {
    @Schema(name = "名称")
    private String name;
    @Schema(name = "请求接口")
    private String url;
}
