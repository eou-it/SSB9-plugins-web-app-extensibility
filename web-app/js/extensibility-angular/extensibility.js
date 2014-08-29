
// xe namespace is for providing eXtensible Environment functionality.
// this will likely be provided as a service and injected
var xe = (function (xe) {

    // create a selector for an element
    xe.selector = function( elementType, name ) {
        return '[data-xe-' + elementType + '=' + name + ']';
    }

    // create a selector for removing an element and its associated labels, etc.
    xe.selectorToRemove = function( elementType, name ) {
        return xe.selector( elementType, name) + ', [data-xe-for="' + name + '"]';
    }

    // xe.extensions = {} // load extensions for each page from server-side config

    // templates define how to create each type of widget
    xe.templates = {

        // should probably convert these to a string templating mechanism, ...${field.name}..., etc.
        'static': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><span title="title text" title="title text" data-xe-field="' + field.name + '">{{' + field.name + '}}</span></div>';
        },
        'literal': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><span title="title text" title="title text" data-xe-field="' + field.name + '">' + field.value + '</span></div>';
        },
        'input': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><input title="title text" data-xe-field="' + field.name + '" ng-model="' + field.name + '"></input></div>';
        },
        'text': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><text title="title text" data-xe-field="' + field.name + '" ng-model="' + field.name + '"></text></div>';
        }
    }

    // utility function to generate the HTML for a specific field
    xe.generateField = function(field) {
        var template = xe.templates[field.template] || xe.templates['static'];
        console.log( 'using template ' + field.template + '=' + template)
        return template( field );
    };

    // Apply configured extensions to an element
    xe.extend = function (element, attributes /*?, transclude?*/) {
        function removeField(fieldName) {
            var element = this;
            var it = $(xe.selectorToRemove('field',fieldName), element);
            console.log('remove', it);
            it.remove();
        }

        function addField(field) {
            var element = this;
            //if field.html is set, use it otherwise generate from template
            var it = field.html?field.html:xe.generateField( field );

            if ( field.after ) {
                $(it).insertAfter( $(xe.selector('field', field.after), element));
            } else {
                $(it).prependTo( element );
            }
            console.log('add', it);
        }

        if (xe.extensions[attributes.xeSubpage]) {
            if (xe.extensions[attributes.xeSubpage].remove)
                xe.extensions[attributes.xeSubpage].remove.map(removeField, element);
            if (xe.extensions[attributes.xeSubpage].add)
                xe.extensions[attributes.xeSubpage].add.map(addField, element);
        }
    };

    return xe;
})(xe || {});

function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function elementToJSON(element) {
    //return 0;

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



angular.module('extensibility', [])
    .run(function($resource){
        console.log('Running - fetching metadata...');
        //load meta-data synchronously to make sure it is available before compile needs it
        $.ajax({
            url: '/PayrollEmployeeProfileSsb/internal/extensions',
            dataType: 'json',
            data: {page: location.pathname.substring(location.pathname.indexOf('/ssb/')+5),hash:location.hash},
            async: false,
            success: function(json){
                console.log('data loaded');
                //
                xe.extensions={};
                for (var i=0;i<json.length; i++) {
                    //convert from array to map with key is subpage and value the modifications
                    xe.extensions[json[i].subpage] = json[i].modifications;
                }
            }
        });
        console.log(xe.extensions);
    })
    .directive('xeExtensible', function($q) {
        function link(scope, element, attrs) {
            //element.css('color', 'red');
        }
        function changeElement(el, index) {
            var m=meta[el.id];
            console.log(el.id);
            if (m) {
                console.log('el['+index+"]="+el);
                console.log(angular.element(el).html());
                if (m.app){
                    angular.element(el).append(m.app);
                }
                if (m.pre){
                    angular.element(el).prepend(m.pre);
                }
                if (m.aft){
                    angular.element(el).after(m.aft);
                }
                if (m.rep){
                    angular.element(el).replaceWith(m.rep);
                }
                if (m.del){
                    angular.element(el).empty();
                }
            }
            //recursively change children
            var ch=angular.element(el).children();
            for (var i=0;i<ch.length;i++){
                changeElement(ch[i],i);
            }
        }
        function changeElement1(el, index) {
            var el2=el.find('#1\\.3');
            //var el2=$('#1\\.3', el)
            console.log(el2);
        }
        return {
            restrict: 'C',
            //priority: 500,
            //link: link,
            compile: function ( element, attributes, transclude ) {
                // How can we get the metadata from an asynchronous promise
                if (xe.extensions) {
                    //console.log('Compile phase:\nOuter HTML\n-------------------\n'+element.context.outerHTML);
                    console.log('Extending sub page '+attributes.xeSubpage)
                    xe.extend( element, attributes, transclude );
                } else {
                    console.log('No page extensions found');
                }
                console.log('Element:',elementToJSON(element));
                link.transclude = transclude;
                return( link );
            }
        }
    });