package org.openea.eap.extj.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 *
 */
@Data
public class UserCommonInfoVO {
    @Schema(name = "用户id")
    private String userId;
    @Schema(name = "用户账号")
    private String userAccount;
    @Schema(name = "用户姓名")
    private String userName;
    @Schema(name = "用户头像")
    private String headIcon;
    @Schema(name = "组织主键")
    private String organizeId;
    @Schema(name = "组织主键集合")
    private List<String> organizeIdList;
    @Schema(name = "组织名称")
    private String organizeName;
    @Schema(name = "岗位")
    private List<UserPositionVO> positionIds;
    @Schema(name = "系统集合")
    private List<UserSystemVO> systemIds;

    private String positionId;

    private String positionName;

    @Schema(name = "上次登录")
    private Integer prevLogin;
    @Schema(name = "上次登录时间",example = "1")
    private Long prevLoginTime;
    @Schema(name = "上次登录IP")
    private String prevLoginIPAddress;
    @Schema(name = "上次登录地址")
    private String prevLoginIPAddressName;
    @Schema(name = "门户id")
    private String portalId;
    /**
     * 当前组织角色+全局角色 Id数组
     */
    private List<String> roleIds;

    /**
     * 当前组织角色+全局角色 名称集合用 , 号隔开
     */
    private String roleName;

    /**
     * 直属主管 (u.RealName + "/" + u.Account)
     */
    private String manager;

    /**
     * 手机
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private Long birthday;

    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 部门名称 结构树
     */
    private String departmentName;

    private Boolean isAdministrator;

    private String signImg;

    private Date changePasswordDate;

    private String systemId;

    private String appSystemId;
}
