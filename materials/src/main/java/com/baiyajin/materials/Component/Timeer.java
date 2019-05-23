package com.baiyajin.materials.Component;


import com.baiyajin.materials.service.PageMaterialUpdateInterface;
import com.baiyajin.materials.service.PageMaterialUpdateService;
import com.baiyajin.util.u.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Timeer {

    @Autowired
    private PageMaterialUpdateService pageMaterialUpdateService;



//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void aadd(){
//        pageMaterialUpdateService.updatePrice();
//        System.out.println("处理完毕当前时间："+ DateFormatUtil.dateToString(new Date()));
//    }

    //
    @Scheduled(cron = "0 13 15 * * ?")
    public void aadd(){
        pageMaterialUpdateService.updatePrice();
    }

}
