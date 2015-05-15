( function() {

    'use strict';

    xe.ng = xe.ng || {};

    xe.ng.applyExtensions = function() {
        return function() {
            return {
                restrict: 'EA',
                compile: function(element) {
                    xe.extend(element);
                }
        };
    };
};

angular.module('extensibility', [])
    .directive( 'body', xe.ng.applyExtensions() )
    .directive( 'xeDynamic', xe.ng.applyExtensions());
})();

