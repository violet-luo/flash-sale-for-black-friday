var cartPanelView = {

    cartCellTemplate: "",

    setup: function(callback) {

        $('.tbar-cart-item').hover(function() { $(this).find('.p-del').show(); }, function() { $(this).find('.p-del').hide(); });
        $('.jth-item').hover(function() { $(this).find('.add-cart-button').show(); }, function() { $(this).find('.add-cart-button').hide(); });

        $('.toolbar-tab').hover(function() {
            $(this).find('.tab-text').html($(this).attr("data"));
            $(this).find('.tab-text').addClass("tbar-tab-hover");
            $(this).find('.footer-tab-text').addClass("tbar-tab-footer-hover");
            $(this).addClass("tbar-tab-selected");
        }, function() {
            $(this).find('.tab-text').removeClass("tbar-tab-hover");
            $(this).find('.footer-tab-text').removeClass("tbar-tab-footer-hover");
            $(this).removeClass("tbar-tab-selected");
        });

        cartPanelView.cartCellTemplate = $("#tbar-cart-item-template").html();

        callback();
    },

    tabItemClick: function(typeName) {
        if ($('.toolbar-wrap').hasClass('toolbar-open')) {
            cartPanelView.tbar_panel_close(typeName);
        } else {
            cartPanelView.tbar_panel_show(typeName);
        }
    },

    // show panel
    tbar_panel_show: function(panelName) {

        $('.toolbar-tab').removeClass('tbar-tab-click-selected');
        $('.tbar-tab-' + panelName).addClass('tbar-tab-click-selected');
        $('.tbar-tab-' + panelName).find('.tab-text').remove();

        $('.toolbar-panel').css('visibility', 'hidden');
        $('.tbar-panel-' + panelName).css({ 'visibility': "visible", "z-index": "1" });
        $('.toolbar-wrap').addClass('toolbar-open');
    },

    // close panel
    tbar_panel_close: function(panelName) {

        if ($('.tbar-tab-' + panelName).find('.tab-text').length > 0) {
            $('.toolbar-tab').each(function(i) {
                var tagValue = $(this).attr("tag");
                if ((tagValue != panelName) && (tagValue != undefined)) {
                    var info = "<em class='tab-text '>" + $(this).attr('data') + "</em>";
                    $(this).append(info);
                    $(this).removeClass('tbar-tab-click-selected');
                    $('.tbar-panel-' + $(this).attr('tag')).css({ 'visibility': "hidden", "z-index": "-1" });
                }
            });
            $('.tbar-tab-' + panelName).addClass('tbar-tab-click-selected');
            $('.tbar-tab-' + panelName).find('.tab-text').remove();
            $('.tbar-panel-' + panelName).css({ 'visibility': "visible", "z-index": "1" });
        } else {
            $('.toolbar-wrap').removeClass('toolbar-open');
            var info = "<em class='tab-text '>" + $('.tbar-tab-' + panelName).attr("data") + "</em>";
            $('.tbar-tab-' + panelName).append(info);
            $('.tbar-tab-' + panelName).removeClass('tbar-tab-click-selected');
            $('.tbar-panel-' + panelName).css({ 'visibility': "hidden", "z-index": "-1" });
        }
    },

    // fill the shopping cart
    fillCart: function(dataJSON) {
        var rowsHtml = "";
        for (var i = 0; i < dataJSON.orders.length; i++) {
            var shops = dataJSON.orders[i];
            for (var x = 0; x < shops.orderItems.length; x++) {
                var it = shops.orderItems[x];
                rowsHtml += String.format(
                    cartPanelView.cartCellTemplate,
                    it.pid,
                    it.title,
                    it.image,
                    it.unitPrice * it.quantity,
                    it.quantity
                );
            }
        }
        $("#cart-list").html(rowsHtml);
        $("#cart-number").html(dataJSON.totalQuantity);
        $("#cart-sum").html(String.format("¥{0}", dataJSON.totalPrices));
        $("#tab-sub-cart-count").html(dataJSON.totalQuantity);
    }
};

$(function() {
    cartPanelView.setup(function() {
        cartPanelView.fillCart(orderData);
    });

});



var orderData = {
    "totalQuantity": 2,
    "totalPrices": 8998,
    "orders": [{
        "shop": "炫龙官方旗舰店",
        "orderItems": [{
                "pid": "10803521657",
                "image": "https://images.unsplash.com/photo-1468436139062-f60a71c5c892?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NTN8fGxhcHRvcHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=800&q=60",
                "title": "MacBook Pro 16 inch",
                "color": "Silver",
                "size": "128 GB",
                "unitPrice": 2499,
                "quantity": 1
            },
            {
                "pid": "10803523232",
                "image": "https://images.unsplash.com/photo-1468436139062-f60a71c5c892?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NTN8fGxhcHRvcHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=800&q=60",
                "title": "MacBook Pro 16 inch",
                "color": "Space Gray",
                "size": "128 GB",
                "unitPrice": 2499,
                "quantity": 1
            }
        ]
    }]
};