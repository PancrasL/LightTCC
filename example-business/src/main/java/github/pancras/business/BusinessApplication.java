package github.pancras.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.spring.RpcReferencePostProcessor;


@SpringBootApplication
public class BusinessApplication {
    @Bean
    public NettyRpcClient nettyRpcClient() {
        return new NettyRpcClient("zookeeper://localhost:2181");
    }

    @Bean
    public RpcReferencePostProcessor rpcReferencePostProcessor(RpcClient rpcClient) {
        return new RpcReferencePostProcessor(rpcClient);
    }

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }
}
