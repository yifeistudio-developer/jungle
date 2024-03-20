package com.yifeistudio.jungle.config

import com.yifeistudio.jungle.adapter.impl.RedisRegistrationManager
import com.yifeistudio.jungle.model.RunningMode
import com.yifeistudio.jungle.service.PeerMessageService
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

    private var serverPort: Int = 0

    private var serverMarker: String = ""

    @Resource
    private lateinit var jungleProperties: JungleProperties

    @Resource
    private lateinit var peerMessageService: PeerMessageService

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
        logger.info("jungle server self-register finished. marker = {}", serverMarker)
        Runtime.getRuntime().addShutdownHook(Thread() {

        })
    }

    override fun onApplicationEvent(event: WebServerInitializedEvent) {
        serverPort = event.webServer.port
    }

    @PreDestroy
    fun onStopping() {
        logger.warn("server is stopping. The task before server-stopped is being executed.")
        if (serverMarker != "") {
            registrationManager.deregister(serverMarker)
            logger.info("server - $serverMarker deregister success.")
        }
    }
}