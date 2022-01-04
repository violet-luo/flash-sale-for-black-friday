+

function($) {
    "use strict";
    var format = function(data) {
        var result = [];
        for (var i = 0; i < data.length; i++) {
            var d = data[i];
            if (d.name === "Please Select") continue;
            result.push(d.name);
        }
        if (result.length) return result;
        return [""];
    };

    var sub = function(data) {
        if (!data.sub) return [""];
        return format(data.sub);
    };

    var getCities = function(d) {
        for (var i = 0; i < raw.length; i++) {
            if (raw[i].name === d) return sub(raw[i]);
        }
        return [""];
    };

    var raw = $.smConfig.rawCitiesData;
    var states = raw.map(function(d) {
        return d.name;
    });
    var initCities = sub(raw[0]);

    var currentState = states[0];
    var currentCity = initCities[0];

    var t;
    var defaults = {

        cssClass: "city-picker",
        rotateEffect: false,

        onChange: function(picker, values, displayValues) {
            var newState = picker.cols[0].value;
            var newCity;
            if (newState !== currentState) {
                clearTimeout(t);

                t = setTimeout(function() {
                    var newCities = getCities(newState);
                    newCity = newCities[0];
                    picker.cols[1].replaceValues(newCities);
                    currentState = newState;
                    currentCity = newCity;
                    picker.updateValue();
                }, 200);
                return;
            }
            newCity = picker.cols[1].value;
            if (newCity !== currentCity) {
                currentCity = newCity;
                picker.updateValue();
            }
        },

        cols: [{
                textAlign: 'center',
                values: states,
                cssClass: "col-state"
            },
            {
                textAlign: 'center',
                values: initCities,
                cssClass: "col-city"
            }
        ]
    };

    $.fn.cityPicker = function(params) {
        return this.each(function() {
            if (!this) return;
            var p = $.extend(defaults, params);

            if (p.value) {
                $(this).val(p.value.join(' '));
            } else {
                var val = $(this).val();
                val && (p.value = val.split(' '));
            }

            if (p.value) {
                if (p.value[0]) {
                    currentState = p.value[0];
                    p.cols[1].values = getCities(p.value[0]);
                }
                if (p.value[1]) {
                    currentCity = p.value[1];
                    p.cols[2].values = getDistricts(p.value[0], p.value[1]);
                } else {
                    p.cols[2].values = getDistricts(p.value[0], p.cols[1].values[0]);
                }!p.value[2] && (p.value[2] = '');
                currentDistrict = p.value[2];
            }
            $(this).picker(p);
        });
    };

}(Zepto);