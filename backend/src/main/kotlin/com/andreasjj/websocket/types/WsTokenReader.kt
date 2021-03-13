package com.andreasjj.websocket.types

import io.micronaut.http.HttpParameters
import io.micronaut.security.token.jwt.cookie.JwtCookieTokenReader
import io.micronaut.security.token.reader.TokenReader
import io.micronaut.security.token.jwt.bearer.BearerTokenConfigurationProperties
import io.micronaut.context.annotation.Requires
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton


@Requires(property = BearerTokenConfigurationProperties.PREFIX + ".enabled", notEquals = StringUtils.FALSE)
@Singleton
class WsTokenReader : TokenReader {
    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(WsTokenReader::class.java)
        private val ORDER = JwtCookieTokenReader.ORDER - 100
    }

    override fun getOrder(): Int {
        return ORDER
    }

    override fun findToken(request: HttpRequest<*>): Optional<String> {
        LOG.debug("Looking for bearer token in params")
        val authorizationHeader =  request.headers.authorization
        val secWebSocketProtocolHeader =  request.headers.get("Sec-WebSocket-Protocol")
        secWebSocketProtocolHeader?.let {
            println(it)
            return Optional.of(it)
        }
        println(authorizationHeader)
        return authorizationHeader
    }
}