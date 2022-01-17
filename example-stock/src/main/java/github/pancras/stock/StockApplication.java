package github.pancras.stock;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import github.pancras.api.StockService;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.spring.RpcServicePostProcessor;
import github.pancras.stock.service.StockServiceImpl;
import github.pancras.tcccore.aspect.TccTryAspect;
import github.pancras.wrapper.RpcServiceConfig;

@SpringBootApplication
public class StockApplication {
    @Bean
    public NettyRpcServer rpcServer() {
        return new NettyRpcServer("localhost:7072", "zookeeper://localhost:2181");
    }

    @Bean
    public TccTryAspect tccTryAspect() {
        return new TccTryAspect();
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(StockApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        NettyRpcServer server = context.getBean(NettyRpcServer.class);
        StockService service = context.getBean(StockServiceImpl.class);
        RpcServiceConfig<StockService> serviceConfig = new RpcServiceConfig.Builder<>(service)
                .serviceName(StockService.class.getCanonicalName())
                .build();
        server.registerService(serviceConfig);
    }
}
