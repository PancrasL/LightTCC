package github.pancras.business.service;

import org.springframework.stereotype.Service;

import github.pancras.api.BalanceService;
import github.pancras.api.OrderService;
import github.pancras.api.StockService;
import github.pancras.tcccore.annotation.TccGlobal;
import github.pancras.tcccore.dto.TccActionContext;
import github.pancras.spring.annotation.RpcReference;

@Service
public class BusinessService {
    @RpcReference
    OrderService orderService;
    @RpcReference
    StockService stockService;
    @RpcReference
    BalanceService balanceService;

    @TccGlobal
    public void createBussiness(TccActionContext context) {
        orderService.createOrder(context);
        stockService.reduceStock(context);
        balanceService.reduceBalance(context);
    }
}