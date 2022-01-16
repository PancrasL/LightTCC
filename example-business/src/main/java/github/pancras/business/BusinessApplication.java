package github.pancras.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.spring.RpcReferencePostProcessor;
import github.pancras.tcccore.aspect.TccGlobalAspect;

@SpringBootApplication

public class BusinessApplication {
    @Bean
    public RpcReferencePostProcessor rpcReferencePostProcessor() {
        NettyRpcClient server = new NettyRpcClient("zookeeper://localhost:2181");
        return new RpcReferencePostProcessor(server);
    }

    @Bean
    public TccGlobalAspect tccGlobalAspect(){
        return new TccGlobalAspect();
    }

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }
}
