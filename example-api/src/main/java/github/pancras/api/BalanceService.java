package github.pancras.api;

import github.pancras.tcccore.dto.TccActionContext;

public interface BalanceService {
    boolean reduceBalance(TccActionContext context);

    boolean commit(TccActionContext context);

    boolean rollback(TccActionContext context);
}
