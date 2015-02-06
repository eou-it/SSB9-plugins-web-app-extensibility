/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

xe.stats.groupCompileCount = 0;

xe.ng={};
xe.ng.deferedSectionTags = {'XE-ACCORDION':true};
xe.ng.priorities={ngIncludeBase:400, ngRepeatBase:1000}; // Priorities for Angualar directives to which we want to hook

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
            section = { section:parent.attributes[xe.typePrefix+xe.type.section].value /*, element:parent*/};
        }
        else if (parent.attributes[xe.attrInh.section]) {
            //Unable to get to the real parent element in the compile phase for this element
            section = { section:parent.attributes[xe.attrInh.section].value /*, element:null*/};
        }
    }
    return section;
};

xe.ng.parseElement = function(page,el) {
    if (el.nodeType!=3) {
        var attributes = xe.ng.attributes(el);
        if (attributes) {
            attributes.element = el;
            attributes.parent = xe.ng.parentElement(el);
            page.addElement(attributes);
        }
    }
};




xe.ng.groupCompile = function(prio, context) {
    return function() {
        return {
            restrict: 'E',
            prio: prio,
            compile: function (element, attributes) {
                xe.stats.groupCompileCount++;
                // check if parent has attribute xeSectionInh
                if (element[0].parentNode && element[0].parentNode.attributes) {
                    var section = element[0].parentNode.attributes[xe.attrInh.section];
                    if (section) {
                        attributes.xeSection = section.value;
                        xe.log('Compile ', context, prio, attributes, xe.logging.level == xe.logging.verbose ? element[0].innerHTML : element);
                        xe.log('Extending inherited section ' + attributes.xeSection);
                        xe.extend(element, attributes);
                    }
                }
            }
        }
    }
};

xe.ng.groupProcessing = function(prio, context) {
    return function() {
        return {
            restrict: 'ECA',
            priority: prio,  //AngularJS ngInclude directive has 400
            link: function ($scope, element, attributes) {
                xe.log('Link ', context, prio, $scope.src||attributes.src||'', '\n', xe.logging.level==xe.logging.verbose?element[0].innerHTML:element);
                xe.extendPagePart(element, attributes);
            }
        }
    }
};

//add secion name to a child of a section (used when parent is not available when compiling child)
xe.ng.inheritSection = function(prio, context) {
    return function() {
        return {
            restrict: 'ECA',
            priority: prio,
            compile:  function (element, attributes)  {
                xe.log('Compile ', context,prio, attributes.src||'', '\n', xe.logging.level==xe.logging.verbose?element[0].innerHTML:element);
                // find parent xe-section
                var section=null;
                for (var parent=element[0].parentNode ;parent && (section == null) && parent.attributes; parent=parent.parentNode) {
                    if (parent.attributes[xe.attr.section]) {
                        section = parent.attributes[xe.attr.section].value;
                    }
                }
                if (section) {
                    element.attr(xe.attrInh.section, section);
                }
            }
        }
    }
};


angular.module('extensibility', [])
    .directive('xeField', function() {
        return {
            restrict: 'A',
            compile: function (element, attributes) {
                xe.ng.parseElement(xe.page,element[0]);
            }
        }
    })
    .directive('xeSection', function() {
        return {
            restrict: 'A',
            compile: function ( element, attributes /*, transclude */ ) {
                xe.ng.parseElement(xe.page,element[0]);
                if ( xe.ng.deferedSectionTags[element[0].tagName] ) {
                    ;// Section processing to happen in a child of this tag
                } else {
                    xe.log('Compile Section', attributes.xeSection);
                    xe.extend(element);
                    /*
                    if (xe.extensions.sections[attributes.xeSection]) {
                        xe.log('Extending section ' + attributes.xeSection);
                        xe.extend(element, attributes);
                    }
                    */
                }
            }
        }
    })
    //Assume pages loaded via ng-include or ui-view start with one of the tags with groupCompile below
    //If the parentNode has xeSectionInh, treat the content as if it were a section with name xeSectionInh
    .directive('div',  xe.ng.groupCompile(0,'div' ))
    .directive('span', xe.ng.groupCompile(0,'span'))
    .directive('form', xe.ng.groupCompile(0,'form'))
    //
    .directive('ngInclude', xe.ng.inheritSection (xe.ng.priorities.ngIncludeBase+1, 'before ng-include')) // before baseline include, add xeSectionInh attribute (priority > baseline )
    .directive('ngInclude', xe.ng.groupProcessing(xe.ng.priorities.ngIncludeBase-1, 'after ng-include' ))  // after  baseline include do the group processing like moving/removing sections
    .directive('uiView'   , xe.ng.groupProcessing(  0,'ui-view'))
    .directive('body'     , xe.ng.groupProcessing(  0,'body'   ))
    .directive('ngRepeat' , xe.ng.inheritSection (   xe.ng.priorities.ngRepeatBase+1, 'before ng-repeat')) // before baseline repeat, add xeSectionInh attribute (priority > baseline )
    //.directive('xeAccordion' , xe.groupLink(0,'xeAccordion',true))
;
