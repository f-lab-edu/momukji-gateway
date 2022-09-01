package kr.flab.momukji.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.flab.momukji.gateway.dto.request.OrderLogDto;
import kr.flab.momukji.gateway.filter.log.LogService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LogController {
    
    private final LogService logService;

    @GetMapping("/checkOrderAmount")
    public String getOrderCount(@RequestBody OrderLogDto orderLogDto) {
        return logService.getOrderAmount(orderLogDto.getStoreId()) + "개 주문되었습니다.";
    }
}
