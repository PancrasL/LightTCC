package github.pancras.balance;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.spring.RpcServicePostProcessor;

@SpringBootApplication
public class BalanceApplication {
    @Bean
    public RpcServicePostProcessor rpcServiceConfig() {
        NettyRpcServer server = new NettyRpcServer("localhost:7000", "zookeeper://localhost:2181");
        return new RpcServicePostProcessor(server);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(BalanceApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
