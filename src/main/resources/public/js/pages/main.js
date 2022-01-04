$(function() {

    'use strict';

    var $distpicker = $('#distpicker');

    $distpicker.distpicker({
        state: 'California',
        city: 'San Francisco'
    });

    $('#reset').click(function() {
        $distpicker.distpicker('reset');
    });

    $('#reset-deep').click(function() {
        $distpicker.distpicker('reset', true);
    });

    $('#destroy').click(function() {
        $distpicker.distpicker('destroy');
    });

});