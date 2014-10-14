// jquery/backbone-specific extensibility code
xe.jq = (function(xe) {
	var jq = xe.jq || {};
  
	//Create a dom structure from handlebars template, do extensions and save the modified template
	jq.extendTemplate = function(template) {
		var element =  $('<div>'+template.text+'</div>');
        $(element[0].firstElementChild).addClass('xe-extended'); // prevent duplicate application
        //Group level extensions
		xe.extendPagePart(element);
        //Section level extensions
		var sections=$(xe.selector(xe.type.section),element);
		sections.each(function (index, section) {
			var attributes={xeSection:section.attributes[xe.attr.section].value };
			xe.extend(section,attributes);
		});
		template.text=element[0].innerHTML;
	};
  
    jq.extendSection = function(sectionElement) {
        xe.extend(sectionElement, {xeSection:$(sectionElement).data(xe.attr.section) } );
    };

    jq.extend = function(rootElement) {
	    if (rootElement && $('.xe-extended',rootElement).length) {
            //the root element has already been extended -- bail out
            return;
        }
	    //Group level extensions
        xe.extendPagePart(rootElement);
        //Section level extensions
        $(xe.selector(xe.type.section), rootElement).each( function(idx, ele) {jq.extendSection(ele);} );
        //Extend handlebars templates
        var templates = $('script[type="text/x-handlebars-template"]',rootElement);
        templates.each(function (index, template){
            xe.jq.extendTemplate(template);
        });
    };

    $( function() {
        xe.jq.extend();
    });

    return jq;
})(xe||{});