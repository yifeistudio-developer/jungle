package com.yifeistudio.jungle.controller

import com.yifeistudio.jungle.adapter.RegistrationManager
import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.model.Peer
import com.yifeistudio.jungle.service.PeerService
import jakarta.annotation.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/cluster")
internal class ClusterController {

    @Resource
    private lateinit var peerService: PeerService

    @Resource
    private lateinit var registrationManager: RegistrationManager

    /**
     * 查询集群信息
     */
    @GetMapping("/profile")
    fun profile(): Mono<ClusterProfile> {
        val profile = peerService.profile()
        return Mono.just(profile)
    }

    @GetMapping("/peers")
    fun peers(): Mono<Set<Peer>> {
        val peers = registrationManager.peers()
        return Mono.just(peers)
    }

}