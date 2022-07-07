package kr.flab.momukji.gateway.external;

import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class Call {
    private WebClient webClient;

    Call(){
        webClient = WebClient.builder().baseUrl("http://localhost:8081").build();
    }

    public Mono<String> getApiHello(String name){
        return webClient.get()
                .uri("/hello/"+name)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getApiIllegal(){
        final Random random = new Random();
        if(random.nextBoolean())
            throw new IllegalArgumentException("IllegalArgumentException 발생");

        return webClient.get()
                .uri("/illegal")
                .retrieve()
                .bodyToMono(String.class);
    }

    public void illegalException(){
        final Random random = new Random();
        if(random.nextBoolean()) {
            log.info("illegalException 발생");
            throw new IllegalArgumentException("IllegalArgumentException 발생");
        }
        log.info("success");
    }
}
