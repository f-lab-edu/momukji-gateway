package kr.flab.momukji.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import kr.flab.momukji.gateway.service.CircuitService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class CircuitController {
    private final CircuitService circuitService;

    @GetMapping("/hello/{name}")
    public Mono<String> getHelloName(@PathVariable String name){
        return circuitService.getHello(name);
    }

    @GetMapping("/illegal")
    public void getIllegal(){
        circuitService.getIllegal();
    }
    
}
