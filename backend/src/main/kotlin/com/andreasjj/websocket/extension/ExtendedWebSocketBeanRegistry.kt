package com.andreasjj.websocket.extension

import com.andreasjj.websocket.extension.annotation.OnClose
import com.andreasjj.websocket.extension.annotation.OnError
import com.andreasjj.websocket.extension.annotation.OnMessage
import com.andreasjj.websocket.extension.annotation.OnOpen
import io.micronaut.context.BeanContext
import io.micronaut.context.Qualifier
import io.micronaut.context.annotation.Replaces
import io.micronaut.inject.MethodExecutionHandle
import io.micronaut.inject.BeanDefinition
import io.micronaut.inject.ExecutionHandle
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.websocket.context.WebSocketBeanRegistry
import io.micronaut.websocket.context.WebSocketBean
import io.micronaut.websocket.exceptions.WebSocketException
import java.util.concurrent.ConcurrentHashMap
import java.util.*

@Replaces(WebSocketBeanRegistry::class)
class ExtendedWebSocketBeanRegistry(beanContext: BeanContext, stereotype: Class<out Annotation?>) : WebSocketBeanRegistry {
    private val beanContext: BeanContext
    private val stereotype: Class<out Annotation?>
    private val webSocketBeanMap: MutableMap<Class<*>, WebSocketBean<*>> = ConcurrentHashMap(3)

    override fun <T> getWebSocket(type: Class<T>): WebSocketBean<T> {
        println("Hello")
        val webSocketBean = webSocketBeanMap[type] as WebSocketBean<T>?
        return if (webSocketBean != null) {
            webSocketBean
        } else {
            val qualifier: Qualifier<T> = Qualifiers.byStereotype<Any>(stereotype) as Qualifier<T>
            val beanDefinition: BeanDefinition<T> = beanContext.getBeanDefinition(type, qualifier)
            val bean: T = beanContext.getBean(type, qualifier)
            val executableMethods = beanDefinition.executableMethods
            var onOpen: MethodExecutionHandle<T, *>? = null
            var onClose: MethodExecutionHandle<T, *>? = null
            var onMessage: MethodExecutionHandle<T, *>? = null
            var onError: MethodExecutionHandle<T, *>? = null
            for (method in executableMethods) {
                if (method.isAnnotationPresent(OnOpen::class.java)) {
                    onOpen = ExecutionHandle.of(
                        bean,
                        method
                    )
                    continue
                }
                if (method.isAnnotationPresent(OnClose::class.java)) {
                    onClose = ExecutionHandle.of(
                        bean,
                        method
                    )
                    continue
                }
                if (method.isAnnotationPresent(OnError::class.java)) {
                    onError = ExecutionHandle.of(
                        bean,
                        method
                    )
                    continue
                }
                if (method.isAnnotationPresent(OnMessage::class.java)) {
                    onMessage = ExecutionHandle.of(
                        bean,
                        method
                    )
                }
            }
            if (onMessage == null) {
                throw WebSocketException("WebSocket handler must specify an @OnMessage handler: $bean")
            }
            val newWebSocketBean = ExtendedWebSocketBean(bean, beanDefinition, onOpen, onClose, onMessage, onError)
            if (beanDefinition.isSingleton) {
                webSocketBeanMap[type] = newWebSocketBean
            }
            newWebSocketBean
        }
    }

    /**
     * Default web socket impl.
     *
     * @author graemerocher
     * @param <T>
    </T> */
    private class ExtendedWebSocketBean<T>(
        private val bean: T,
        private val definition: BeanDefinition<T>,
        private val onOpen: MethodExecutionHandle<T, *>?,
        private val onClose: MethodExecutionHandle<T, *>?,
        private val onMessage: MethodExecutionHandle<T, *>,
        private val onError: MethodExecutionHandle<T, *>?
    ) : WebSocketBean<T> {
        override fun getBeanDefinition(): BeanDefinition<T> {
            println("Hello2")
            return definition
        }

        override fun getTarget(): T {
            println("Hello3")
            return bean
        }

        override fun messageMethod(): Optional<MethodExecutionHandle<T, *>> {
            println("Hello4")
            return Optional.of(onMessage)
        }

        override fun closeMethod(): Optional<MethodExecutionHandle<T, *>> {
            println("Hello5")
            return Optional.ofNullable(onClose)
        }

        override fun openMethod(): Optional<MethodExecutionHandle<T, *>> {
            println("Hello6")
            return Optional.ofNullable(onOpen)
        }

        override fun errorMethod(): Optional<MethodExecutionHandle<T, *>> {
            println("Hello7")
            return Optional.ofNullable(onError)
        }
    }

    /**
     * Default constructor.
     * @param beanContext The bean context
     * @param stereotype Stereotype to use for bean lookup
     */
    init {
        println("Hello8")
        this.beanContext = beanContext
        this.stereotype = stereotype
    }
}
