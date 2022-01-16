package github.pancras.balance.service;

import github.pancras.api.BalanceService;
import github.pancras.spring.annotation.RpcService;
import github.pancras.tcccore.annotation.TccTry;
import github.pancras.tcccore.dto.TccActionContext;

@RpcService
public class BalanceServiceImpl implements BalanceService {
    @TccTry
    @Override
    public boolean reduceBalance(TccActionContext context) {
        System.out.println("reduce balance");
        return false;
    }

    @Override
    public boolean commit(TccActionContext context) {
        System.out.println("reduce commit");
        return false;
    }

    @Override
    public boolean rollback(TccActionContext context) {
        System.out.println("reduce rollback");
        return false;
    }
}
