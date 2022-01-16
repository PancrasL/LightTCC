package github.pancras.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.spring.RpcServicePostProcessor;

@SpringBootApplication
public class OrderApplication {
    @Bean
    public RpcServicePostProcessor rpcServiceConfig() {
        NettyRpcServer server = new NettyRpcServer("localhost:7001", "zookeeper://localhost:2181");
        return new RpcServicePostProcessor(server);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(OrderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
