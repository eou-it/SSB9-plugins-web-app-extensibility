
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
    //Note HvT:
    //If we use templates, it is not possible to insert fields having the same structure as the existing application fields,
    //unless the templates are configurable too
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

        function getType(param) {
            if (param.section)
                return 'section';
            else if (param.field)
                return 'field';
            else
                throw 'Error: section or field is required for the current operation';
        }

        function add(param) {
            var element = this;
            var type = getType(param);
            var it;
            if (param.component) {
                it = xe.renderComponent(param.component);
                console.log('Component HTML: ',it);
            } else { //if param.html is set, use it otherwise generate from template
                it = param.html ? param.html : xe.generateField(param);
            }
            if ( param.after ) {
                $(it).insertAfter( $(xe.selector(type, param.after), element));
            } else if (param.before){
                $(it).insertBefore( $(xe.selector(type, param.before), element));
            } else {
                $(it).prependTo( element );
            }
            console.log('add', it);
        }

        function remove(param) {
            var element = this;
            var type = getType(param);
            var it = $(xe.selectorToRemove(type,param[type]), element);
            console.log('remove', it);
            it.remove();
        }

        function move(param) {
            var element = this;
            var type = getType(param);

            var elementToMove = $(xe.selector(type, param[type], element));
            if ( param.after ) {
                $(elementToMove).insertAfter( $(xe.selector(type, param.after), element));
            } else if ( param.before ) {
                $(elementToMove).insertBefore( $(xe.selector(type, param.before), element));
            } else { // move to first field in parent

            }
            console.log('move', elementToMove);
        }

        function replace(param) {
            var element = this;
            var type = getType(param);
            var it = $(xe.selectorToRemove(type,param[type]), element);
            var to = null;
            if (param.component) {
                to = xe.renderComponent(param.component);
            } else { //if param.html is set, use it otherwise generate from template
                to = param.html ? param.html : xe.generateField(param);
            }
            console.log('replace', it);
            it.replaceWith(to);
        }


        var start = new Date().getTime();
        if (xe.extensions[attributes.xeSubpage]) {
            if (xe.extensions[attributes.xeSubpage].remove)
                xe.extensions[attributes.xeSubpage].remove.map(remove, element);
            if (xe.extensions[attributes.xeSubpage].add)
                xe.extensions[attributes.xeSubpage].add.map(add, element);
            if (xe.extensions[attributes.xeSubpage].move)
                xe.extensions[attributes.xeSubpage].move.map(move, element);
            if (xe.extensions[attributes.xeSubpage].replace)
                xe.extensions[attributes.xeSubpage].replace.map(replace, element);
        }
        console.log("Time to process extensions/ms: "+(new Date().getTime()-start));
    };


    //HTML rendering support
    xe.isVoidElement = function(tag) {
        //xe.voidElements is a map to specify the html element types that are 'void' (have no content)
        if (!xe.voidElements) {
            console.log('initialize void elements');
            xe.voidElements = {};
            //Initialize voidElements map
            ["area", "base", "br", "col", "command", "embed", "hr", "img", "input",
                "keygen", "link", "meta", "param", "source", "track", "wbr"].forEach(function (val, idx, array) {
                    xe.voidElements[val] = true;
                }
            );
        }
        return xe.voidElements[tag.toLowerCase()];
    }

    //Render a generic component as HTML (a component is simplified representation of HTML DOM element)
    xe.renderComponent = function ( component ) {
        //Note: void elements don't need </tag> and have no content
        var result="";
        if (component.tagName) {
            result="<"+component.tagName;
            Object.getOwnPropertyNames(component.attributes).forEach(function(val,idx,array) {
                    result+=' '+val+'="'+component.attributes[val]+'"';
                }
            );
            result += ">";
            //recursively add the child nodes
            if (component.childNodes && !xe.isVoidElement(component.tagName) ) {
                component.childNodes.forEach(function (val, idx, array) {
                        result += ' ' + xe.renderComponent(val);
                    }
                );
            }
        }
        if (component.textContent && (!component.tagName || !xe.isVoidElement(component.tagName))) {
            result += component.textContent;
        }
        if (component.tagName && !xe.isVoidElement(component.tagName)){
            result += "</" + component.tagName + ">";
        }
        return result;
    }

    return xe;
})(xe || {});

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



angular.module('extensibility', [])
    .run(function($resource){
        console.log('Running - fetching metadata...');
        //load meta-data synchronously to make sure it is available before compile needs it.
        //TODO: do not hard code application and /ssb/ here to avoid being application and Banner specific
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
        return {
            restrict: 'C',
            compile: function ( element, attributes, transclude ) {
                if (xe.extensions) {
                    console.log('Extending sub page '+attributes.xeSubpage);
                    xe.extend( element, attributes, transclude );
                } else {
                    console.log('No page extensions found for '+attributes.xeSubpage);
                }
                link.transclude = transclude;
                return( link );
            }
        }
    });