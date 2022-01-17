package github.pancras.order;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import github.pancras.api.OrderService;
import github.pancras.order.service.OrderServiceImpl;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.spring.RpcServicePostProcessor;
import github.pancras.tcccore.aspect.TccTryAspect;
import github.pancras.wrapper.RpcServiceConfig;

@SpringBootApplication
public class OrderApplication {
    @Bean
    public NettyRpcServer rpcServer() {
        return new NettyRpcServer("localhost:7071", "zookeeper://localhost:2181");
    }

    @Bean
    public TccTryAspect tccTryAspect() {
        return new TccTryAspect();
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(OrderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        NettyRpcServer server = context.getBean(NettyRpcServer.class);
        OrderService service = context.getBean(OrderServiceImpl.class);
        RpcServiceConfig<OrderService> serviceConfig = new RpcServiceConfig.Builder<>(service)
                .serviceName(OrderService.class.getCanonicalName())
                .build();
        server.registerService(serviceConfig);
    }
}
