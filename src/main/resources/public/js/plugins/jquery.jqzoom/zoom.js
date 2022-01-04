function tabs(tabId, tabNum) {
    $(tabId + " .tab li").removeClass("curr");
    $(tabId + " .tab li").eq(tabNum).addClass("curr");
    $(tabId + " .tabcon").hide();
    $(tabId + " .tabcon").eq(tabNum).show();
}

// Mouse over preview image
function preview(img) {
    $("#preview .jqzoom img").attr("src", $(img).attr("src"));
    $("#preview .jqzoom img").attr("jqimg", $(img).attr("bimg"));
}

// Image magnifier
$(function() {
    $(".jqzoom").jqueryzoom({ xzoom: 400, yzoom: 400 });
});

// Preview image movement
$(function() {
    var tempLength = 0; // length of current move
    var viewNum = 5; // set the number of images to be displayed at a time
    var moveNum = 2; // numbers of move per movement
    var moveTime = 300; // move speed in ms
    var scrollDiv = $(".spec-scroll .items ul"); // container with move items
    var scrollItems = $(".spec-scroll .items ul li"); // scroll items in the container
    var moveLength = scrollItems.eq(0).width() * moveNum; // calculate the length of each move
    var countLength = (scrollItems.length - viewNum) * scrollItems.eq(0).width(); // calculate the total length

    // next image
    $(".spec-scroll .next").bind("click", function() {
        if (tempLength < countLength) {
            if ((countLength - tempLength) > moveLength) {
                scrollDiv.animate({ left: "-=" + moveLength + "px" }, moveTime);
                tempLength += moveLength;
            } else {
                scrollDiv.animate({ left: "-=" + (countLength - tempLength) + "px" }, moveTime);
                tempLength += (countLength - tempLength);
            }
        }
    });

    // last image
    $(".spec-scroll .prev").bind("click", function() {
        if (tempLength > 0) {
            if (tempLength > moveLength) {
                scrollDiv.animate({ left: "+=" + moveLength + "px" }, moveTime);
                tempLength -= moveLength;
            } else {
                scrollDiv.animate({ left: "+=" + tempLength + "px" }, moveTime);
                tempLength = 0;
            }
        }
    });
});