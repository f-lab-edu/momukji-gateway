package kr.flab.momukji.gateway.service;

import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import kr.flab.momukji.gateway.external.Call;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CircuitService {
    private final Call call;
    private static final String DEFAULT_NAME = "circuit";
    private static final String ILLEGAL_FALLBACK_DEFAULT = "illegalFallback";
    private static final String FALLBACK_DEFAULT = "helloFallback";

    @CircuitBreaker(name = DEFAULT_NAME, fallbackMethod = FALLBACK_DEFAULT)
    public Mono<String> getHello(String name){
        return call.getApiHello(name);
    }

    @CircuitBreaker(name = DEFAULT_NAME)
    @Retry(name = DEFAULT_NAME, fallbackMethod = ILLEGAL_FALLBACK_DEFAULT)
    public void getIllegal(){
        log.info("getIllegal start");
        call.illegalException();
    }

    private void illegalFallback(Throwable t){
        log.error("Retry 중 오류");
    }

    private Mono<String> helloFallback(String name, Throwable t){
        log.error("Fallback : "+ t.getMessage());
        return Mono.just("fallback data");
    }
}
