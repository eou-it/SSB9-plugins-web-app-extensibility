xe.stats.groupCompileCount = 0;

xe.groupCompile = function(prio, context) {
    return function() {
        if (xe.devMode()) {
            return {
                restrict: 'E',
                compile: function (element, attributes) {
                    if (xe.stats.groupCompileCount == 0)
                        xe.stats.t0 = new Date();
                    xe.parseGroups(element, attributes);
                    xe.stats.groupCompileCount++;
                    if (xe.stats.groupCompileCount == 100)
                        xe.stats.t100 = new Date() - xe.stats.t0;
                }
            }
        } else {
            return {compile: null};
        }
    }
};

xe.groupLink = function(prio, context) {
    return function() {
        return {
                restrict: 'ECA',
                link: function ($scope, element, attributes) {
                    console.log('Link ', context,attributes.src||'', '\n', element);
                    xe.extendPagePart(element, attributes);
                },
                priority: prio  //AngularJS ngInclude directive has 400
              }
    }
};

angular.module('extensibility', [])
    .run(function(){
        xe.startup();
    })
    .directive('xeSection', function() {
        return {
            restrict: 'A',
            compile: function ( element, attributes /*, transclude */ ) {
                console.log('Compile Section ',attributes.xeSection);
                if (xe.extensions.sections[attributes.xeSection]) {
                    if (window.location.search.indexOf("baseline=y")==-1) {
                        console.log('Extending section ' + attributes.xeSection);
                        xe.extend(element, attributes);
                    }
                }
            }
        }
    })
    //A number of tags that can be a group (common parent for a collection of sections)
    //In developer mode parse the page structure in  groupCompile
    .directive( 'div', xe.groupCompile())
    .directive('span', xe.groupCompile())
    .directive('form', xe.groupCompile())
    //Group level changes are implemented in groupLink
    .directive('ngInclude', xe.groupLink(399,'ng-include'))
    .directive('uiView'   , xe.groupLink(0,'ui-view'))
    .directive('body'     , xe.groupLink(0,'page'))
;