package github.pancras.api;

import github.pancras.tcccore.dto.TccActionContext;

public interface StockService {
    boolean reduceStock(TccActionContext context);

    boolean commit(TccActionContext context);

    boolean rollback(TccActionContext context);
}
