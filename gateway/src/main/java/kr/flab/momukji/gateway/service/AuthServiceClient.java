package kr.flab.momukji.gateway.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import kr.flab.momukji.gateway.dto.request.TokenDto;
import kr.flab.momukji.gateway.dto.response.CommonResponse;

@FeignClient(name = "auth")
public interface AuthServiceClient {
    
    @PostMapping(value = "/api/validate")
    public ResponseEntity<CommonResponse> validate(@SpringQueryMap TokenDto tokenDto);
}
