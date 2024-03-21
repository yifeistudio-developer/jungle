package com.yifeistudio.jungle.config

import com.yifeistudio.jungle.adapter.impl.RedisRegistrationManager
import com.yifeistudio.jungle.model.RunningMode
import com.yifeistudio.jungle.service.PeerService
import com.yifeistudio.jungle.util.Networks
import jakarta.annotation.PreDestroy
import jakarta.annotation.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class AfterServerStarted : ApplicationRunner, ApplicationListener<WebServerInitializedEvent> {

    // 服务端口号
    private var serverPort: Int = 0

    // 节点标识： IP_ADDRESS@SERVER_PORT
    private var serverMarker: String = ""

    @Resource
    private lateinit var jungleProperties: JungleProperties

    @Resource
    private lateinit var peerMessageService: PeerService

    @Resource
    private lateinit var registrationManager: RedisRegistrationManager

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun run(args: ApplicationArguments?) {
        if (jungleProperties.mode == RunningMode.STANDALONE) {
            logger.info("server running in standalone mode.")
            return
        }
        val cluster = jungleProperties.cluster
        val ipPrefer = cluster.ipPrefer
        val ipAddress = Networks.localIpAddress(ipPrefer)
        serverMarker = "${ipAddress}@$serverPort"
        registrationManager.register(serverMarker)
        peerMessageService.start().block()
        logger.info("jungle server self-register finished. marker = {}", serverMarker)
    }

    /**
     * 填充当前web服务实际监听端口
     */
    override fun onApplicationEvent(event: WebServerInitializedEvent) {
        serverPort = event.webServer.port
    }

    /**
     * 停机时关闭资源
     */
    @PreDestroy
    fun onStopping() {
        if (serverMarker == "") {
            return
        }
        logger.warn("server is stopping. The task before server-stopped is being executed.")
        registrationManager.deregister(serverMarker)
        peerMessageService.stop().block()
        logger.info("server - $serverMarker deregister success.")
    }
}
///~