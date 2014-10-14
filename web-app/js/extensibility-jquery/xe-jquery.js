// jquery/backbone-specific extensibility code
xe.jq = (function(xe) {
	var jq = xe.jq || {};

  
	//Create a dom structure from handlebars template, do extensions and save the modified template
	jq.extendTemplate = function(template) {
		var element =  $('<div>'+template.text+'</div>');
		//Do group level changes on templates
		xe.extendPagePart(element);

		//Do section level changes
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
	/Do group level changes
    xe.extendPagePart(rootElement);
  
    $(xe.selector(xe.type.section), rootElement).each( function(idx, ele) {jq.extendSection(ele);} );
  }

  $( function() {
    xe.jq.extend();
  });

  return jq;
})(xe||{});