package org.openea.eap.extj.model.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class AppInfoModel {
    @Schema(name = "用户id")
    private String id;
    @Schema(name = "用户账号")
    private String account;
    @Schema(name = "用户姓名")
    private String realName;
    @Schema(name = "用户头像")
    private String headIcon;
    @Schema(name = "组织名称")
    private String organizeName;
    @Schema(name = "部门名称")
    private String departmentName;
    @Schema(name = "角色名称")
    private String roleName;
    @Schema(name = "岗位名称")
    private String positionName;
    @Schema(name = "性别")
    private Integer gender;
    @Schema(name = "生日")
    private Long birthday;
    @Schema(name = "手机号码")
    private String mobilePhone;
    @Schema(name = "邮箱")
    private String email;

}
