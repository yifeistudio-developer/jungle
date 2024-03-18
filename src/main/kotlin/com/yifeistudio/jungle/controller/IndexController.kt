package com.yifeistudio.jungle.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping
class IndexController {


    @GetMapping("/health")
    fun checkHealth():Mono<Any> {
        return Mono.just("health")
    }

}