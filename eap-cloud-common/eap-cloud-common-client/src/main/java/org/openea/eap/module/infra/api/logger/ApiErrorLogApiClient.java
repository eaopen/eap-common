package org.openea.eap.module.infra.api.logger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openea.eap.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import org.openea.eap.module.infra.enums.ApiConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
@ConditionalOnMissingBean(ApiErrorLogApi.class)
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - API 异常日志")
public interface ApiErrorLogApiClient extends ApiErrorLogApi {

    String PREFIX = ApiConstants.PREFIX + "/api-error-log";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建 API 异常日志")
    void createApiErrorLog(@Valid @RequestBody ApiErrorLogCreateReqDTO createDTO);

}
