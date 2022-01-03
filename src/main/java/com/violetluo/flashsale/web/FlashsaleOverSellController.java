package com.violetluo.flashsale.web;

import com.violetluo.flashsale.service.FlashsaleActivityService;
import com.violetluo.flashsale.services.FlashsaleOverSellService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FlashsaleOverSellController {

    @Autowired
    private FlashsaleOverSellService flashsaleOverSellService;



    /**
     * Process flash sale request
     * @param flashsaleActivityId
     * @return
     */
//    @ResponseBody
//    @RequestMapping("/flashsale/{flashsaleActivityId}")
    public String  seckil(@PathVariable long flashsaleActivityId){
        return flashsaleOverSellService.processFlashsale(flashsaleActivityId);
    }
    @Autowired
    private FlashsaleActivityService flashsaleActivityService;

    /**
     * Process flash sale request with Lua
     * @param flashsaleActivityId
     * @return
     */
    @ResponseBody
    @RequestMapping("/flashsale/{flashsaleActivityId}")
    public String flashsaleItem(@PathVariable long flashsaleActivityId) {
        boolean stockValidateResult = flashsaleActivityService.flashsaleStockValidator(flashsaleActivityId);
        return stockValidateResult ? "Congrats!" : "Sorry, this item is out of stock.";
    }
}
