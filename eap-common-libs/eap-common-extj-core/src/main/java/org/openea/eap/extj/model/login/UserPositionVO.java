package org.openea.eap.extj.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class UserPositionVO {
    @Schema(name = "岗位id")
    private String id;
    @Schema(name = "岗位名称")
    private String name;
}
