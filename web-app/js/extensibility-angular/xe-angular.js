


function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

//Not so well working method for getting a JSON representation of a DOM element
function elementToJSON(element) {
    var filter = function(key, value) {
        var keys = {"":true,"id":true, "class":true, "name":true, "type":true, "value":true, "tagName":true,
                    "attributes":true, "childNodes":true, /*"nodeName":true,*/ "nodeType":true, "title":true };
        if (value) {
            if (key==="childNodes") {
                console.log('Process childNode', value);
            }
            if (value.nodeType === 3 || value === {}) {
                //console.log('Remove', value)
                return undefined
            }
            if (isNumeric(key))
                return value;
            if (keys[key] && value)
                return value;
        }
        return undefined;
    }

    var j=JSON.stringify(element[0],filter,2);
    return j;
}

xe.stats.groupFnCount = 0;
xe.groupFn = function() {
    return {
        restrict: 'E',
        compile: function ( element, attributes, transclude ) {
            if (xe.stats.groupFnCount == 0)
                xe.stats.t0 = new Date();
            xe.extendGroups(element,attributes);
            xe.stats.groupFnCount++;
            if (xe.stats.groupFnCount == 100)
                xe.stats.t100= new Date()-xe.stats.t0;

        }
    }
};

angular.module('extensibility', [])
    .run(function($resource){
        xe.startup();
    })
    .directive('xeSection', function() {
        function link(scope, element, attrs) {
            //element.css('color', 'red');
        }
        return {
            restrict: 'A',
            compile: function ( element, attributes, transclude ) {
                if (xe.extensions.sections[attributes.xeSection]) {
                    if (window.location.search.indexOf("baseline=y")==-1) {
                        console.log('Extending section ' + attributes.xeSection);
                        xe.extend(element, attributes);
                    }
                } else {
                    console.log('No section extensions found for '+attributes.xeSection);
                }
                link.transclude = transclude;
                return( link );
            }
        }
    })
    //A number of tags that can be a group (common parent for a collection of sections)
    .directive( 'div', xe.groupFn)
    .directive('span', xe.groupFn)
    .directive('form', xe.groupFn)

;