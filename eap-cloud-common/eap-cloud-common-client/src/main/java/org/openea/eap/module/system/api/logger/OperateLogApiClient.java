package org.openea.eap.module.system.api.logger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openea.eap.framework.common.pojo.CommonResult;
import org.openea.eap.framework.common.pojo.PageResult;
import org.openea.eap.module.system.api.logger.dto.OperateLogCreateReqDTO;
import org.openea.eap.module.system.api.logger.dto.OperateLogPageReqDTO;
import org.openea.eap.module.system.api.logger.dto.OperateLogRespDTO;
import org.openea.eap.module.system.enums.ApiConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@Service
@ConditionalOnMissingBean(OperateLogApi.class)
@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 操作日志")
public interface OperateLogApiClient extends OperateLogApi {

    String PREFIX = ApiConstants.PREFIX + "/operate-log";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建操作日志")
    void createOperateLog(@Valid @RequestBody OperateLogCreateReqDTO createReqDTO);

    @GetMapping(PREFIX + "/page")
    @Operation(summary = "获取指定模块的指定数据的操作日志分页")
    PageResult<OperateLogRespDTO> getOperateLogPage(@SpringQueryMap OperateLogPageReqDTO pageReqDTO);

}
