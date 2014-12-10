// jquery/backbone-specific extensibility code
xe.jq = (function(xe) {
	var jq = xe.jq || {};

	//Create a dom structure from handlebars templates, do extensions and save the modified templates
	jq.extendTemplates = function(rootElement) {
        var templates = $('script[type="text/x-handlebars-template"]',rootElement);
        templates.each(function (index, template){
            var rootElement =  $('<div>'+template.text+'</div>');
            $(rootElement[0].firstElementChild).addClass('xe-extended'); // prevent duplicate application
            //Group level extensions
            xe.extendPagePart(rootElement);
            //Section level extensions
            $(xe.selector(xe.type.section), rootElement).each(function (index, section) {
                jq.extendSection(section);
            });
            template.text=rootElement[0].innerHTML;
        });
	};

    jq.extendSection = function(sectionElement) {
        xe.extend(sectionElement, {xeSection:$(sectionElement).attr(xe.attr.section) } );
    };

    jq.extend = function(rootElement) {
	    if (rootElement && $('.xe-extended',rootElement).length) {
            //the root element has already been extended -- bail out
            return;
        }
	    //Group level extensions
        xe.extendPagePart(rootElement);
        //Section level extensions
        var sections = $(xe.selector(xe.type.section), rootElement);
        //include the top level element if it is a section
        if ($(rootElement).is(xe.selector(xe.type.section))) {sections = sections.add( rootElement );}
        sections.each( function(idx, ele) {jq.extendSection(ele);} );
        //Handlebars template extensions
        jq.extendTemplates(rootElement);
    };

    $( function() {
        xe.jq.extend();
    });

    return jq;
})(xe||{});
//Reordering of Tabs
$.fn.tabs = _.wrap($.fn.tabs, function expandTabs(org) {
    var self = this;
    var sections = xe.extensions.sections;
    var list = this.find("ol,ul").eq(0);
    $.each( sections, function(key, section  ) {   //Iterate through array of JSON objects (extensions)
        if(typeof section.nextSibling!=='undefined') {
            var listItemToBeMoved = list.children(xe.selectorFor('section', section.name));
            if (listItemToBeMoved.length != 0) {
                if (section.nextSibling) {   // If nextSibling is not null
                    var listItemTo = list.children(xe.selectorFor('section', section.nextSibling));
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


