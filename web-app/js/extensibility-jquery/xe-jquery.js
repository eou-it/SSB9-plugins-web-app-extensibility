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
    };


    /***************************************************************************************************
    Extensions are specified in the JSON metadata at either the section level or the field level
    This function processes section level extensions
    ***************************************************************************************************/
    jq.extend = function( rootElement ) {

        if (xe.extensionsFound) {
            if ( xe.enableExtensions() ) {
                xe.extend( rootElement );
                xe.jq.extendTemplates( rootElement );
            }
        }
    };


    $( function() {
        xe.jq.extend( $('body') );
    });

    return jq;
})(xe||{});

if (xe.extensionsFound) {
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

    function getExtensions(xeFieldType, section, component) {

        var extensions = null, sectionExtensions = section;
        if (xeFieldType == xe.attr.section && (sectionExtensions == undefined || null == sectionExtensions)) {
            // fetch the xe section extensions if any
            var sectionName = component.closest(xe.selector(xe.type.section)).attr(xe.typePrefix + xe.type.section);
            extensions = sectionName && _.findWhere(xe.extensions.sections, {name: sectionName});
        } else if (xeFieldType == xe.attr.field && typeof sectionExtensions == "object") {
            // fetch the xe field extensions if any
            var fieldName = component.closest(xe.selector(xe.type.field)).attr(xe.attr.field);
            if (sectionExtensions.fields) {
                extensions = fieldName && _.findWhere(sectionExtensions.fields, {name: fieldName});
            }
        }
        return extensions;
    }

    // Extend the jquery editable function to provide dynamic placeholder and title values
    var oldEditable = $.fn.editable;

    $.fn.editable = _.wrap($.fn.editable, function (origEditable){
        var args = Array.prototype.slice.call( arguments, 1 );
        var element = this;
        // find the xe field and section name
        var sectionExtensions = getExtensions(xe.attr.section, null, element);
        var settings = null, fieldExtensions = null;
        if(sectionExtensions){
            fieldExtensions = getExtensions(xe.attr.field, sectionExtensions, element);
            if ( fieldExtensions && fieldExtensions.attributes) {
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

    //An extension of the select2 jquery plugin function... (EXTZ- 594)
    var originalSelect2 = $.fn.select2;

    $.fn.select2 = _.wrap($.fn.select2, function extendSelect2(origSelect2) {
        var args = Array.prototype.slice.call(arguments, 1);
        var element = this;
        var select2Component = origSelect2.apply(this, args);

        //check if any extensions have been applied for this component
        var sectionExtensions = getExtensions(xe.attr.section, null, element);
        var fieldExtensions = getExtensions(xe.attr.field, sectionExtensions, element);
        if (fieldExtensions && fieldExtensions.attributes) {
            //manipulate the select2 component to set the hint text
            var select2 = select2Component.data('select2');
            if (fieldExtensions.attributes.title) {
                select2.container.attr("title", xe.i18n(fieldExtensions.attributes.title));
            }
        }

        return select2Component;

    });
    $.extend($.fn.select2, originalSelect2);

}
