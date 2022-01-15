package github.pancras.order.service;

import github.pancras.api.OrderService;
import github.pancras.tcccore.annotation.TccTry;
import github.pancras.tcccore.dto.TccActionContext;

public class OrderServiceImpl implements OrderService {
    @TccTry
    @Override
    public boolean createOrder(TccActionContext context) {
        return false;
    }

    @Override
    public boolean commit(TccActionContext context) {
        return false;
    }

    @Override
    public boolean rollback(TccActionContext context) {
        return false;
    }
}
