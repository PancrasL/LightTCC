package github.pancras.stock.service;

import org.springframework.stereotype.Service;

import github.pancras.api.StockService;
import github.pancras.spring.annotation.RpcService;
import github.pancras.tcccore.annotation.TccTry;
import github.pancras.tcccore.dto.TccActionContext;

@Service
public class StockServiceImpl implements StockService {
    @TccTry
    @Override
    public boolean reduceStock(TccActionContext context) {
        System.out.println("reduce Stock");
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
