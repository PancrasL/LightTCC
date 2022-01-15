package github.pancras.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.spring.RpcServicePostProcessor;

@SpringBootApplication
public class OrderApplication {
    @Bean
    public NettyRpcServer nettyRpcServer() {
        return new NettyRpcServer("localhost:7998", "zookeeper://localhost:2181");
    }

    @Bean
    public RpcServicePostProcessor rpcServiceConfig(RpcServer rpcServer) {
        return new RpcServicePostProcessor(rpcServer);
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(OrderApplication.class, args);
        NettyRpcServer nettyRpcServer = context.getBean(NettyRpcServer.class);
        nettyRpcServer.start();
    }
}
