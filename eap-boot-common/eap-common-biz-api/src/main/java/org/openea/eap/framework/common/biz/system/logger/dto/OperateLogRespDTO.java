package org.openea.eap.framework.common.biz.system.logger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OperateLogRespDTO {

    @Schema(description = "模块类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "订单")
    private String type;

    @Schema(description = "模块数据编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "188")
    private Long bizId;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    private Long userId;
}
