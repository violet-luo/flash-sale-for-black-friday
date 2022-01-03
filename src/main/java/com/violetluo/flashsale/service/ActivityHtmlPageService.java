package com.violetluo.flashsale.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.dao.FlashsaleItemDao;
import com.violetluo.flashsale.db.po.FlashsaleActivity;
import com.violetluo.flashsale.db.po.FlashsaleItem;

@Slf4j
@Service
public class ActivityHtmlPageService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    @Autowired
    private FlashsaleItemDao flashsaleItemDao;

    /**
     * Create HTML page
     *
     * @throws Exception
     */
    public void createActivityHtml(long flashsaleActivityId) {

        PrintWriter writer = null;
        try {
            FlashsaleActivity flashsaleActivity = flashsaleActivityDao.queryFlashsaleActivityById(flashsaleActivityId);
            FlashsaleItem flashsaleItem = flashsaleItemDao.queryFlashsaleItemById(flashsaleActivity.getItemId());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("flashsaleActivity", flashsaleActivity);
            resultMap.put("flashsaleItem", flashsaleItem);
            resultMap.put("flashsalePrice", flashsaleActivity.getFlashsalePrice());
            resultMap.put("oldPrice", flashsaleActivity.getOldPrice());
            resultMap.put("itemId", flashsaleActivity.getItemId());
            resultMap.put("itemName", flashsaleItem.getItemName());
            resultMap.put("itemDesc", flashsaleItem.getItemDesc());

            // create Thymeleaf context
            Context context = new Context();
            context.setVariables(resultMap);

            // output
            File file = new File("src/main/resources/templates/" + "flashsale_item_" + flashsaleActivityId + ".html");
            writer = new PrintWriter(file);
            // implement page static method
            templateEngine.process("flashsale_item", context, writer);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("Static page failed: " + flashsaleActivityId);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}

