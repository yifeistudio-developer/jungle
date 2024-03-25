package com.yifeistudio.jungle.config

import com.yifeistudio.jungle.model.RunningMode
import com.yifeistudio.jungle.service.PeerService
import com.yifeistudio.jungle.util.Networks
import jakarta.annotation.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * 服务启动后
 *
 * - 服务可以以单点或集群模式启动
 * - 集群模式启动下则需要启动伙伴关系管理
 */
@Component
class AfterServerStarted : ApplicationRunner, ApplicationListener<WebServerInitializedEvent> {

    // 服务端口号
    private var serverPort: Int = 0

    @Resource
    private lateinit var peerMessageService: PeerService

    @Resource
    private lateinit var jungleProperties: JungleProperties

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 应用启动后
     *
     * - 根据配置判断是否取消集群服务
     */
    override fun run(args: ApplicationArguments?) {
        logger.info("server running in ${jungleProperties.mode} mode.")
        if (jungleProperties.mode == RunningMode.STANDALONE) {
            return
        }
        if (serverPort < 1024) {
            logger.warn("running in cluster mode but server port is illegal $serverPort")
            return
        }
        val cluster = jungleProperties.cluster
        val ipPrefer = cluster.ipPrefer
        val ipAddress = Networks.localIpAddress(ipPrefer)
        val serverMarker = "${ipAddress}@$serverPort"
        peerMessageService.launch(serverMarker).doOnSuccess {
            logger.info("peer service is started. the will running in cluster mode.")
        }.onErrorResume {
            exp -> logger.error("launch peer service failed. the server will running in standalone mode. -", exp)
            Mono.empty()
        }.block()
    }

    /**
     * 填充当前web服务实际监听端口
     */
    override fun onApplicationEvent(event: WebServerInitializedEvent) {
        serverPort = event.webServer.port
    }

}
///~