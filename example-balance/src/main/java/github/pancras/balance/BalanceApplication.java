package github.pancras.balance;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import github.pancras.api.BalanceService;
import github.pancras.balance.service.BalanceServiceImpl;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.tcccore.aspect.TccTryAspect;
import github.pancras.wrapper.RpcServiceConfig;

@SpringBootApplication
public class BalanceApplication {
    @Bean
    public NettyRpcServer rpcServer() {
        return new NettyRpcServer("localhost:7000", "zookeeper://localhost:2181");
    }

    @Bean
    public TccTryAspect tccTryAspect() {
        return new TccTryAspect();
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(BalanceApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        NettyRpcServer server = context.getBean(NettyRpcServer.class);
        BalanceServiceImpl service = context.getBean(BalanceServiceImpl.class);
        RpcServiceConfig<BalanceServiceImpl> serviceConfig = new RpcServiceConfig.Builder<>(service)
                .serviceName(BalanceService.class.getCanonicalName())
                .build();
        server.registerService(serviceConfig);
    }
}
