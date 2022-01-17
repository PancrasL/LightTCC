package github.pancras.balance.service;

import org.springframework.stereotype.Service;

import github.pancras.api.BalanceService;
import github.pancras.tcccore.annotation.TccTry;
import github.pancras.tcccore.dto.TccActionContext;

@Service
public class BalanceServiceImpl implements BalanceService {
    @TccTry
    @Override
    public boolean reduceBalance(TccActionContext context) {
        System.out.println("reduce balance");
        return true;
    }

    @Override
    public boolean commit(TccActionContext context) {
        System.out.println("reduce commit");
        return true;
    }

    @Override
    public boolean rollback(TccActionContext context) {
        System.out.println("reduce rollback");
        return true;
    }
}
