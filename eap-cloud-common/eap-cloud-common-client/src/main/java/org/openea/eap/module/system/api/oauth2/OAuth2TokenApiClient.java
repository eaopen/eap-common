package org.openea.eap.module.system.api.oauth2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openea.eap.framework.common.pojo.CommonResult;
import org.openea.eap.module.system.api.logger.OperateLogApi;
import org.openea.eap.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import org.openea.eap.module.system.api.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import org.openea.eap.module.system.api.oauth2.dto.OAuth2AccessTokenRespDTO;
import org.openea.eap.module.system.enums.ApiConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Service
@ConditionalOnMissingBean(OAuth2TokenApi.class)
@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - OAuth2.0 令牌")
public interface OAuth2TokenApiClient extends OAuth2TokenApi {

    String PREFIX = ApiConstants.PREFIX + "/oauth2/token";

    /**
     * 校验 Token 的 URL 地址，主要是提供给 Gateway 使用
     */
    @SuppressWarnings("HttpUrlsUsage")
    String URL_CHECK = "http://" + ApiConstants.NAME + PREFIX + "/check";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建访问令牌")
    OAuth2AccessTokenRespDTO createAccessToken(@Valid @RequestBody OAuth2AccessTokenCreateReqDTO reqDTO);

    @GetMapping(PREFIX + "/check")
    @Operation(summary = "校验访问令牌")
    @Parameter(name = "accessToken", description = "访问令牌", required = true, example = "tudou")
    OAuth2AccessTokenCheckRespDTO checkAccessToken(@RequestParam("accessToken") String accessToken);

    @DeleteMapping(PREFIX + "/remove")
    @Operation(summary = "移除访问令牌")
    @Parameter(name = "accessToken", description = "访问令牌", required = true, example = "tudou")
    OAuth2AccessTokenRespDTO removeAccessToken(@RequestParam("accessToken") String accessToken);

    @PutMapping(PREFIX + "/refresh")
    @Operation(summary = "刷新访问令牌")
    @Parameters({
        @Parameter(name = "refreshToken", description = "刷新令牌", required = true, example = "haha"),
        @Parameter(name = "clientId", description = "客户端编号", required = true, example = "eapyuanma")
    })
    OAuth2AccessTokenRespDTO refreshAccessToken(@RequestParam("refreshToken") String refreshToken,
                                                              @RequestParam("clientId") String clientId);

}
