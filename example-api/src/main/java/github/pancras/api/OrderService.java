package github.pancras.api;

import github.pancras.tcccore.dto.TccActionContext;

public interface OrderService {
    boolean createOrder(TccActionContext context);

    boolean commit(TccActionContext context);

    boolean rollback(TccActionContext context);
}
