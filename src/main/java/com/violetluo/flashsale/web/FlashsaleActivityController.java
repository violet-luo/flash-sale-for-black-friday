package com.violetluo.flashsale.web;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.violetluo.flashsale.db.dao.OrderDao;
import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.dao.FlashsaleItemDao;
import com.violetluo.flashsale.db.po.Order;
import com.violetluo.flashsale.db.po.FlashsaleActivity;
import com.violetluo.flashsale.db.po.FlashsaleItem;
import com.violetluo.flashsale.service.FlashsaleActivityService;
import com.violetluo.flashsale.util.RedisService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class FlashsaleActivityController {

    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    @Autowired
    private FlashsaleItemDao flashsaleItemDao;

    @Autowired
    FlashsaleActivityService flashsaleActivityService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    RedisService redisService;


    @RequestMapping("/addFlashsaleActivity")
    public String addFlashsaleActivity() {
        return "add_activity";
    }

    @RequestMapping("/addFlashsaleActivityAction")
    public String addFlashsaleActivityAction(
            @RequestParam("name") String name,
            @RequestParam("itemId") long itemId,
            @RequestParam("flashsalePrice") BigDecimal flashsalePrice,
            @RequestParam("oldPrice") BigDecimal oldPrice,
            @RequestParam("flashsaleNumber") long flashsaleNumber,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            Map<String, Object> resultMap
    ) throws ParseException {
        startTime = startTime.substring(0, 10) +  startTime.substring(11);
        endTime = endTime.substring(0, 10) +  endTime.substring(11);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
        FlashsaleActivity flashsaleActivity = new FlashsaleActivity();
        flashsaleActivity.setName(name);
        flashsaleActivity.setItemId(itemId);
        flashsaleActivity.setFlashsalePrice(flashsalePrice);
        flashsaleActivity.setOldPrice(oldPrice);
        flashsaleActivity.setTotalStock(flashsaleNumber);
        flashsaleActivity.setAvailableStock(new Integer("" + flashsaleNumber));
        flashsaleActivity.setLockStock(0L);
        flashsaleActivity.setActivityStatus(1);
        flashsaleActivity.setStartTime(format.parse(startTime));
        flashsaleActivity.setEndTime(format.parse(endTime));
        flashsaleActivityDao.inertFlashsaleActivity(flashsaleActivity);
        resultMap.put("flashsaleActivity", flashsaleActivity);
        return "add_success";
    }

    @RequestMapping("/flashsales")
    public String activityList(Map<String, Object> resultMap) {
        try (Entry entry = SphU.entry("flashsales")) {
            List<FlashsaleActivity> flashsaleActivities = flashsaleActivityDao.queryFlashsaleActivitysByStatus(1);
            resultMap.put("flashsaleActivities", flashsaleActivities);
            return "flashsale_activity";
        } catch (BlockException ex) {
            log.error("Checking the list of falsh sales is limited "+ex.toString());
            return "wait";
        }
    }


    @RequestMapping("/item/{flashsaleActivityId}")
    public String itemPage(Map<String, Object> resultMap, @PathVariable long flashsaleActivityId) {
        FlashsaleActivity flashsaleActivity;
        FlashsaleItem flashsaleItem;

        String flashsaleActivityInfo = redisService.getValue("flashsaleActivity:" + flashsaleActivityId);
        if (StringUtils.isNotEmpty(flashsaleActivityInfo)) {
            log.info("Redis cache data:" + flashsaleActivityInfo);
            flashsaleActivity = JSON.parseObject(flashsaleActivityInfo, FlashsaleActivity.class);
        } else {
            flashsaleActivity = flashsaleActivityDao.queryFlashsaleActivityById(flashsaleActivityId);
        }

        String flashsaleItemInfo = redisService.getValue("flashsaleItem:" + flashsaleActivity.getItemId());
        if (StringUtils.isNotEmpty(flashsaleItemInfo)) {
            log.info("Redis cache data:" + flashsaleItemInfo);
            flashsaleItem = JSON.parseObject(flashsaleActivityInfo, FlashsaleItem.class);
        } else {
            flashsaleItem = flashsaleItemDao.queryFlashsaleItemById(flashsaleActivity.getItemId());
        }

        resultMap.put("flashsaleActivity", flashsaleActivity);
        resultMap.put("flashsaleItem", flashsaleItem);
        resultMap.put("flashsalePrice", flashsaleActivity.getFlashsalePrice());
        resultMap.put("oldPrice", flashsaleActivity.getOldPrice());
        resultMap.put("itemId", flashsaleActivity.getItemId());
        resultMap.put("itemName", flashsaleItem.getItemName());
        resultMap.put("itemDesc", flashsaleItem.getItemDesc());
        return "flashsale_item";
    }

    /**
     * Process flash sale request
     * @param userId
     * @param flashsaleActivityId
     * @return
     */
    @RequestMapping("/flashsale/buy/{userId}/{flashsaleActivityId}")
    public ModelAndView flashsaleItem(@PathVariable long userId, @PathVariable long flashsaleActivityId) {
        boolean stockValidateResult = false;

        ModelAndView modelAndView = new ModelAndView();
        try {
            /*
             * Check if the user is in the purchased list
             */
            if (redisService.isInLimitMember(flashsaleActivityId, userId)) {
                // if yes
                modelAndView.addObject("resultInfo", "Sorry, you're already in the purchased list.");
                modelAndView.setViewName("flashsale_result");
                return modelAndView;
            }
            /*
             * Check if flash sale can proceed
             */
            stockValidateResult = flashsaleActivityService.flashsaleStockValidator(flashsaleActivityId);
            if (stockValidateResult) {
                Order order = flashsaleActivityService.createOrder(flashsaleActivityId, userId);
                modelAndView.addObject("resultInfo","Congrats! Creating order... Order ID: " + order.getOrderNo());
                modelAndView.addObject("orderNo",order.getOrderNo());
            } else {
                modelAndView.addObject("resultInfo","Sorry, this item is out of stock.");
            }
        } catch (Exception e) {
            log.error("Flash sale system anomaly " + e.toString());
            modelAndView.addObject("resultInfo","Flash sale failed.");
        }
        modelAndView.setViewName("flashsale_result");
        return modelAndView;
    }

    /**
     * Check order
     * @param orderNo
     * @return
     */
    @RequestMapping("/flashsale/orderQuery/{orderNo}")
    public ModelAndView orderQuery(@PathVariable String orderNo) {
        log.info("Check order number: " + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        ModelAndView modelAndView = new ModelAndView();

        if (order != null) {
            modelAndView.setViewName("order");
            modelAndView.addObject("order", order);
            FlashsaleActivity flashsaleActivity = flashsaleActivityDao.queryFlashsaleActivityById(order.getFlashsaleActivityId());
            modelAndView.addObject("flashsaleActivity", flashsaleActivity);
        } else {
            modelAndView.setViewName("order_wait");
        }
        return modelAndView;
    }

    /**
     * Pay order
     * @return
     */
    @RequestMapping("/flashsale/payOrder/{orderNo}")
    public String payOrder(@PathVariable String orderNo) throws Exception {
        flashsaleActivityService.payOrderProcess(orderNo);
        return "redirect:/flashsale/orderQuery/" + orderNo;
    }

    /**
     * Get current server-side time
     * @return
     */
    @ResponseBody
    @RequestMapping("/flashsale/getSystemTime")
    public String getSystemTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        return date;
    }
}
