xe.stats.groupCompileCount = 0;

xe.logging = {none: 0, debug: 1, verbose: 2};
xe.logging.level = xe.logging.none;

xe.ng={};
xe.ng.deferedSectionTags = {'XE-ACCORDION':true};
xe.ng.priorities={ngIncludeBase:400}; // Priorities for directives

xe.ng.attributes = function(element) {
    var result = null;
    if (element.attributes[xe.typePrefix+xe.type.field])
        result =  {field:element.attributes[xe.typePrefix+xe.type.field].value};
    else if (element.attributes[xe.typePrefix+xe.type.section])
        result = {section:element.attributes[xe.typePrefix+xe.type.section].value};
    return result;
};

xe.ng.parentElement = function(element){
    var section=null;
    for (var parent=element.parentNode ;parent && (section == null) && parent.attributes; parent=parent.parentNode) {
        if (parent.attributes[xe.typePrefix+xe.type.section]) {
            section = { section:parent.attributes[xe.typePrefix+xe.type.section].value, element:parent};
        }
//        else if (parent.attributes['data-xe-section-inh']) {
//            //Unable to get to the real parent element in the compile phase for this element
//            section = { name:parent.attributes['data-xe-section-inh'].value, section:null};
//        }
    }
    return section;
}

xe.ng.parseElement = function(page,el) {
    if (el.nodeType!=3) {
        var ch = el.childNodes;
        var attributes = xe.ng.attributes(el);
        if (attributes) {
            attributes.element=el;
            attributes.parent= xe.ng.parentElement(el);
            console.log('Attributes: ', attributes);
            page.addElement(attributes);
        }
    }
};




xe.groupCompile = function(prio, context) {
    return function() {
        return {
            restrict: 'E',
            prio: prio,
            compile: function (element, attributes) {
                // check if parent has attribute xeSectionInh
                if (element[0].parentNode && element[0].parentNode.attributes) {
                    var section = element[0].parentNode.attributes['data-xe-section-inh'];
                    if (section) {
                        attributes.xeSection = section.value;
                        if (xe.logging.level > xe.logging.none) {
                            console.log('Compile ', context, prio, attributes, xe.logging.level == xe.logging.verbose ? element[0].innerHTML : element);
                            console.log('Extending inherited section ' + attributes.xeSection);
                        }
                        xe.extend(element, attributes);
                    }
                }
            }
        }
    }
};

xe.groupProcessing = function(prio, context) {
    return function() {
        return {
            restrict: 'ECA',
            priority: prio,  //AngularJS ngInclude directive has 400
            link: function ($scope, element, attributes) {
                if (xe.logging.level > xe.logging.none)
                    console.log('Link ', context, prio, $scope.src||attributes.src||'', '\n', xe.logging.level==xe.logging.verbose?element[0].innerHTML:element);
                xe.extendPagePart(element, attributes);
            }
        }
    }
};

xe.inheritSection = function(prio, context) {
    return function() {
        return {
            restrict: 'ECA',
            priority: prio,  //AngularJS ngInclude directive has 400
            compile:  function (element, attributes)  {

                if (xe.logging.level > xe.logging.none)
                    console.log('Compile ', context,prio, attributes.src||'', '\n', xe.logging.level==xe.logging.verbose?element[0].innerHTML:element);
                // find parent xe-section
                var section=null;
                for (var parent=element[0].parentNode ;parent && (section == null) && parent.attributes; parent=parent.parentNode) {
                    if (parent.attributes['data-xe-section']) {
                        section = parent.attributes['data-xe-section'].value;
                    }
                }
                if (section) {
                    element.attr('data-xe-section-inh', section);
                }
            }
        }
    }
};

xe.linkSectionOrField = function ($scope, element, attributes) {
    xe.ng.parseElement(xe.page,element[0]);
}

angular.module('extensibility', [])
    .run(function(){

        if ( window.location.search.indexOf("xeLogging=debug")!=-1 )
            xe.logging.level=xe.logging.debug;
        if ( window.location.search.indexOf("xeLogging=verbose")!=-1 )
            xe.logging.level=xe.logging.verbose;
        xe.startup();

    })
    .directive('xeField', function() {
        return {
            restrict: 'A',
            link: xe.linkSectionOrField
        }
    })
    .directive('xeSection', function() {
        return {
            restrict: 'A',
            compile: function ( element, attributes /*, transclude */ ) {
                if ( xe.ng.deferedSectionTags[element[0].tagName] ) {
                    return xe.linkSectionOrField; // Section processing to happen in a child of this tag
                }
                if (xe.logging.level > xe.logging.none)
                    console.log('Compile Section',attributes.xeSection);
                if (xe.extensions.sections[attributes.xeSection]) {
                    if (xe.logging.level > xe.logging.none)
                        console.log('Extending section ' + attributes.xeSection);
                    xe.extend(element, attributes);
                }
                return xe.linkSectionOrField;
            }
        }
    })
    //Assume pages loaded via ng-include or ui-view start with one of the tags with groupCompile below
    //If the parentNode has xeSectionInh, treat the content as if it were a section with name xeSectionInh
    //In developer mode do some of the page parsing
    .directive( 'div', xe.groupCompile(0,'div' ))
    .directive('span', xe.groupCompile(0,'span'))
    .directive('form', xe.groupCompile(0,'form'))
    //
    .directive('ngInclude', xe.inheritSection (xe.ng.priorities.ngIncludeBase+1, 'before ng-include')) // before baseline include, add xeSectionInh attribute (priority > baseline ng-include)
    .directive('ngInclude', xe.groupProcessing(xe.ng.priorities.ngIncludeBase-1, 'after ng-include' ))  // after  baseline include do the group processing like moving/removing sections
    .directive('uiView'   , xe.groupProcessing(  0,'ui-view'))
    .directive('body'     , xe.groupProcessing(  0,'body'   ))
    //.directive('xeAccordion' , xe.groupLink(0,'xeAccordion',true))
;