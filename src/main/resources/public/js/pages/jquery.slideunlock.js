;
(function($, window, document, undefined) {
    function SliderUnlock(elm, options, success) {
        var me = this;
        var $elm = me.checkElm(elm) ? $(elm) : $;
        success = me.checkFn(success) ? success : function() {};

        var opts = {
            successLabelTip: "Successfully Verified",
            duration: 200,
            swipestart: false,
            min: 0,
            max: $elm.width(),
            index: 0,
            IsOk: false,
            lableIndex: 0
        };

        opts = $.extend(opts, options || {});

        me.elm = $elm;
        me.opts = opts;
        me.swipestart = opts.swipestart;
        me.min = opts.min;
        me.max = opts.max;
        me.index = opts.index;
        me.isOk = opts.isOk;
        me.labelWidth = me.elm.find('#label').width();
        me.sliderBg = me.elm.find('#slider_bg');
        me.lableIndex = opts.lableIndex;
        me.success = success;
    }

    SliderUnlock.prototype.init = function() {
        var me = this;

        me.updateView();
        me.elm.find("#label").on("mousedown", function(event) {
            var e = event || window.event;
            me.lableIndex = e.clientX - this.offsetLeft;
            me.handerIn();
        }).on("mousemove", function(event) {
            me.handerMove(event);
        }).on("mouseup", function(event) {
            me.handerOut();
        }).on("mouseout", function(event) {
            me.handerOut();
        }).on("touchstart", function(event) {
            var e = event || window.event;
            me.lableIndex = e.originalEvent.touches[0].pageX - this.offsetLeft;
            me.handerIn();
        }).on("touchmove", function(event) {
            me.handerMove(event, "mobile");
        }).on("touchend", function(event) {
            me.handerOut();
        });
    };

    /**
     * Slide mouse
     */
    SliderUnlock.prototype.handerIn = function() {
        var me = this;
        me.swipestart = true;
        me.min = 0;
        me.max = me.elm.width();
    };

    /**
     * Move out mouse
     */
    SliderUnlock.prototype.handerOut = function() {
        var me = this;
        me.swipestart = false;
        if (me.index < me.max) {
            me.reset();
        }
    };

    /**
     * Move mouse
     * @param event
     * @param type
     */
    SliderUnlock.prototype.handerMove = function(event, type) {
        var me = this;
        if (me.swipestart) {
            event.preventDefault();
            event = event || window.event;
            if (type == "mobile") {
                me.index = event.originalEvent.touches[0].pageX - me.lableIndex;
            } else {
                me.index = event.clientX - me.lableIndex;
            }
            me.move();
        }
    };

    /**
     * Mouse movement process
     */
    SliderUnlock.prototype.move = function() {
        var me = this;
        if ((me.index + me.labelWidth) >= me.max) {
            me.index = me.max - me.labelWidth - 2;
            me.swipestart = false;
            me.isOk = true;
        }
        if (me.index < 0) {
            me.index = me.min;
            me.isOk = false;
        }
        if (me.index + me.labelWidth + 2 == me.max && me.max > 0 && me.isOk) {
            $('#label').unbind().next('#labelTip').
            text(me.opts.successLabelTip).css({ 'color': '#fff' });

            me.success();
        }
        me.updateView();
    };


    /**
     * Update view
     */
    SliderUnlock.prototype.updateView = function() {
        var me = this;

        me.sliderBg.css('width', me.index);
        me.elm.find("#label").css("left", me.index + "px")
    };

    /**
     * Reset the start point of slide
     */
    SliderUnlock.prototype.reset = function() {
        var me = this;

        me.index = 0;
        me.sliderBg.animate({ 'width': 0 }, me.opts.duration);
        me.elm.find("#label").animate({ left: me.index }, me.opts.duration)
            .next("#lableTip").animate({ opacity: 1 }, me.opts.duration);
        me.updateView();
    };

    /**
     * Check if the element exists
     * @param elm
     * @returns {boolean}
     */
    SliderUnlock.prototype.checkElm = function(elm) {
        if ($(elm).length > 0) {
            return true;
        } else {
            throw "this element does not exist.";
        }
    };

    /**
     * Check if the param is function
     * @param fn
     * @returns {boolean}
     */
    SliderUnlock.prototype.checkFn = function(fn) {
        if (typeof fn === "function") {
            return true;
        } else {
            throw "the param is not a function.";
        }
    };

    window['SliderUnlock'] = SliderUnlock;
})(jQuery, window, document);