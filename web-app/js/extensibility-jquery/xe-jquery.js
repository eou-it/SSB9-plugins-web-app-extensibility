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
        $(xe.selector(xe.type.section), rootElement).each( function(idx, ele) {jq.extendSection(ele);} );
        //Handlebars template extensions
        jq.extendTemplates(rootElement);
    };

    $( function() {
        xe.jq.extend();
    });

    return jq;
})(xe||{});