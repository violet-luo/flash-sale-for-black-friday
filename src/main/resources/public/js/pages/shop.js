$(function() {
    var lileg = $(".sui-nav").children().length;
    if (lileg < 8) {
        $("#li-1").css({ "display": "none" });
    }
})

$(document).ready(function() {
    var num;
    $('.sui-nav>li[id]').hover(function() {
        $(this).children().removeClass().addClass('hover-up');
        var Obj = $(this).attr('id');
        num = Obj.substring(3, Obj.length);
        $('#box-' + num).slideDown(300);
    }, function() {
        $(this).children().removeClass().addClass('hover-down');
        $('#box-' + num).hide();
    });
    $('.hidden-box').hover(function() {
        $('#li-' + num).children().removeClass().addClass('hover-up');
        $(this).show();
    }, function() {
        $(this).slideUp(200);
        $('#li-' + num).children().removeClass().addClass('hover-down');
    });
});

$(function() {
    var navH = $("#headnav-fixed").offset().top;
    $(window).scroll(function() {
        var scroH = $(this).scrollTop();
        if (scroH >= navH) {
            $("#headnav-fixed").css({ "position": "fixed", "top": 0, "width": "inherit", "border-bottom": "1px solid #B1191A" });
        } else if (scroH < navH) {
            $("#headnav-fixed").css({ "position": "static", "border-bottom": 0 });
        }
    })
})