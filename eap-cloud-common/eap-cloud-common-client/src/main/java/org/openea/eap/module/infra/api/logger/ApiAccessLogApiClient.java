package org.openea.eap.module.infra.api.logger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openea.eap.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import org.openea.eap.module.infra.enums.ApiConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
@ConditionalOnMissingBean(ApiAccessLogApi.class)
@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - API 访问日志")
public interface ApiAccessLogApiClient extends ApiAccessLogApi{

    String PREFIX = ApiConstants.PREFIX + "/api-access-log";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建 API 访问日志")
    void createApiAccessLog(@Valid @RequestBody ApiAccessLogCreateReqDTO createDTO);

}
