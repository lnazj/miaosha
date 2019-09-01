package com.miaoshaproject.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import javax.security.auth.login.Configuration;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-22
 */
// 当Spring容器没有TomcatEmbededServletContainerFactory这个bean时，会把此Bean加载进Spring
@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory configurableWebServerFactory) {
        //  使用对应工厂类提供给我们的接口定制化我们的tomcat connector
        ((TomcatServletWebServerFactory)configurableWebServerFactory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
               Http11NioProtocol protocol=(Http11NioProtocol) connector.getProtocolHandler();

               // 30秒内没有请求则服务端自动断开连接
               protocol.setKeepAliveTimeout(30000);

               // 当客户端发送超过10000个请求则自动断开keepAlive连接
               protocol.setMaxKeepAliveRequests(10000);

            }
        });

    }
}
