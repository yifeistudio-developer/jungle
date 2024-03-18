package com.yifeistudio.jungle

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@SpringBootTest
class JungleApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun websocketUserEndpointTest() {
		val client = ReactorNettyWebSocketClient()
		val url = URI("ws://localhost:8080/user-endpoint/message")
		client.execute(url) { session ->
			val output = session.send(Flux.empty<String>().map(session::textMessage))
				.repeat(3).then()
			val then = session.receive()
				.doOnNext(::println).then()
			Mono.zip(output, then).then()
		}.block()
	}

}
