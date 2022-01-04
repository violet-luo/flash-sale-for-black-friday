$(function() {
    var $selectorState = $("#state");
    var $selectorCity = $("#city");

    var defaultState = $selectorState.attr('data-default');
    var defaultCity = $selectorCity.attr('data-default');

    if (!defaultState) defaultState = currentState;
    if (!defaultCity) defaultCity = currentCity;

    initSelector($selectorState, states);
    initSelector($selectorCity, getCities(defaultState));

    $selectorState.change(function() {
        currentState = $(this).val();
        initSelector($selectorCity, getCities(currentState));
        $selectorCity.trigger('change');
    })

    $selectorCity.change(function() {
        currentCity = $(this).val();
    })

    function initSelector(selectObj, data) {
        if (data == "") {
            selectObj.hide();
            selectObj.html("");
        } else {
            selectObj.show();
        }

        var str = "";
        var selected = selectObj.attr('data-default');
        for (var i = 0; i < data.length; i++) {
            var _data = data[i];
            if (_data === selected) {
                str += '<option selected="selected" value="' + _data + '">' + _data + '</option>';
            } else {
                str += '<option value="' + _data + '">' + _data + '</option>';
            }
        }
        selectObj.html(str);
    }
})