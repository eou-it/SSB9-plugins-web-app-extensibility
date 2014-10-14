// jquery/backbone-specific extensibility code

xe.jq={};

//Create a dom structure from handlebars template, do extensions and save the modified template
xe.jq.extendTemplate = function(template) {
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


$(document).ready(function () {
    xe.startup();
    xe.log('Start extensions');

    //Do group level changes on body
    var element = $('body');
    xe.extendPagePart(element);

    //Do section level changes on body
    var sections=$(xe.selector(xe.type.section),element);
    sections.each(function (index, section) {
        var attributes={xeSection:section.attributes[xe.attr.section].value };
        xe.extend(section,attributes);
    });

    //Extend handlebars templates
    var templates = $('script[type="text/x-handlebars-template"]');
    templates.each(function (index, template){
        xe.jq.extendTemplate(template);
    });
});