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

var raw = rawCitiesData;

var states = raw.map(function(d) {
    return d.name;
});
var initCities = sub(raw[0]);

var currentState = states[0];
var currentCity = initCities[0];