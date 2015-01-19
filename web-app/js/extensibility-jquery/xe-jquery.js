// jquery/backbone-specific extensibility code
xe.jq = (function(xe) {

	var jq = xe.jq || {};

    /***************************************************************************************************
    Create a dom structure from handlebars templates, do extensions and save the modified templates
    ***************************************************************************************************/
    jq.extendTemplates = function(rootElement) {

        if ( xe.enableExtensions() ) {
            var templates = $('script[type="text/x-handlebars-template"]',rootElement);
            templates.each(function (template){
                var rootElement =  $('<div>'+template.text+'</div>');
                $(rootElement[0].firstElementChild).addClass('xe-extended'); // prevent duplicate application
                xe.extend( rootElement );
                template.text=rootElement[0].innerHTML;
            });
        }
    }


    /***************************************************************************************************
    Extensions are specified in the JSON metadata at either the section level or the field level
    This function processes section level extensions
    ***************************************************************************************************/
    jq.extend = function( rootElement ) {

        if ( xe.enableExtensions() ) {
            xe.extend( rootElement );
            xe.jq.extendTemplates( rootElement );
        }
    };


    $( function() {
        xe.jq.extend( $('body') );
    });

    return jq;
})(xe||{});

//Reordering of Tabs
$.fn.tabs = _.wrap($.fn.tabs, function expandTabs(org) {
    var self = this;
    var sections = xe.extensions.orderedSections;
    var list = this.find("ol,ul").eq(0);
    $.each( sections, function(key, section  ) {   //Iterate through array of JSON objects (extensions)
        if(typeof section.nextSibling!=='undefined') {
            var listItemToBeMoved = list.children(xe.selectorFor(section.name));
            if (listItemToBeMoved.length != 0) {
                if (section.nextSibling) {   // If nextSibling is not null
                    var listItemTo = list.children(xe.selectorFor(section.nextSibling));
                    if (listItemTo.length == 0) {    // If nextSibling not found
                        xe.errors.push('Unable to find target element. ' + JSON.stringify(section));
                        return null;
                    } else {           //If nextSibling found, add a class to moved element and perform insertBefore operation
                        listItemToBeMoved.addClass('xe-moved');
                        listItemToBeMoved.insertBefore(listItemTo);
                    }
                } else {  // If nextSibling is null
                    if (!list) {
                        xe.errors.push('Unable to find element section. ' + section.name);
                        return null;
                    } else {
                        // nextSibling specified as null so becomes last element.
                        list.append(listItemToBeMoved);
                        listItemToBeMoved.addClass('xe-moved');
                    }
                }
            }
        }
    });
    var args = Array.prototype.slice.call(arguments, 1);  // use Array.slice function to copy the arguments array from 1-end, without 'org'
    return org.apply( this, args );
});
// Extend jQuery UI Layout options
$.fn.layout = _.wrap($.fn.layout, function extendLayout(origLayout) {
    var args = Array.prototype.slice.call(arguments, 1);
    var layouts = xe.extensions.layouts;
    if (layouts) {
        for (var i = 0; i < layouts.length; i++) {
            if (xe.extensions.layouts[i][this.selector]) {
                var newargs = $.extend(true, {}, args[0], xe.extensions.layouts[i][this.selector]);
                args[0] = newargs;
            }
        }
    }
    return origLayout.apply( this, args );
});

// Extend the jquery editable function to provide dynamic placeholder and title values
var oldEditable = $.fn.editable;

$.fn.editable = _.wrap($.fn.editable, function (origEditable){
    var args = Array.prototype.slice.call( arguments, 1 );
    // find the xe field and section names
    var fieldName = this.closest(xe.selector(xe.type.field)).attr(xe.typePrefix + xe.type.field);
    var sectionName = this.closest(xe.selector(xe.type.section)).attr(xe.typePrefix + xe.type.section );
    // look for extensions for this section and field
    var sectionExtensions = sectionName && _.findWhere(xe.extensions.sections,{name: sectionName});
    var settings = null, fieldExtensions = null;
    if (sectionExtensions && sectionExtensions.fields) {
        fieldExtensions = fieldName && _.findWhere(sectionExtensions.fields,{name: fieldName});
        if (fieldExtensions.attributes) {
            settings = $.extend(true, {}, args[1]);
            if (fieldExtensions.attributes.placeholder) {
                settings.placeholder = xe.i18n(fieldExtensions.attributes.placeholder);
            }
            if (fieldExtensions.attributes.title) {
                settings.tooltip = xe.i18n(fieldExtensions.attributes.title);
            }
            args[1] = settings;
        }
    }
    // call original editable function with updated arguments
    return origEditable.apply( this, args );

});
// copy extra attributes from original function
$.extend( $.fn.editable, oldEditable );


