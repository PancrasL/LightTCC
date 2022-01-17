package github.pancras.order.service;

import org.springframework.stereotype.Service;

import github.pancras.api.OrderService;
import github.pancras.spring.annotation.RpcService;
import github.pancras.tcccore.annotation.TccTry;
import github.pancras.tcccore.dto.TccActionContext;

@Service
public class OrderServiceImpl implements OrderService {
    @TccTry
    @Override
    public boolean createOrder(TccActionContext context) {
        System.out.println("create Order");
        return true;
    }

    @Override
    public boolean commit(TccActionContext context) {
        System.out.println("Order commit");
        return true;
    }

    @Override
    public boolean rollback(TccActionContext context) {
        System.out.println("Order rollback");
        return true;
    }
}
