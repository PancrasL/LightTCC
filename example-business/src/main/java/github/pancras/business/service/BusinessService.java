package github.pancras.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.pancras.tcccore.annotation.TccGlobal;
import github.pancras.tcccore.dto.TccActionContext;

@Service
public class BusinessService {
    @Autowired
    private BusinessHandler handler;

    /**
     * 由于@TccGlobal使用了aspectJ技术，BusinessServer Bean会被CGlib动态代理，LightRPC的RpcReferencePostProcessor中获得的businessService
     * Bean是一个代理对象， 这导致无法获取到@RpcReference Field，所以我将@RpcRefence放在不会被动态代理的BusinessHandler类中。
     */
    @TccGlobal
    public void createBussiness(TccActionContext context) {
        handler.doCreateBusiness(context);
    }
}