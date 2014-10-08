xe.stats.groupCompileCount = 0;

xe.logging = {none: 0, debug: 1, verbose: 2};
xe.logging.level = xe.logging.none;

xe.ng={};
xe.ng.deferedSectionTags = {'XE-ACCORDION':true};
xe.ng.priorities={ngIncludeBase:400}; // Priorities for directives

xe.groupCompile = function(prio, context, logging) {
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
                        if (logging > xe.logging.none) {
                            console.log('Compile ', context, prio, attributes, logging == xe.logging.verbose ? element[0].innerHTML : element);
                            console.log('Extending inherited section ' + attributes.xeSection);
                        }
                        xe.extend(element, attributes);
                    }
                }
                if (xe.devMode()) {
                    // this needs enhancement
                    if (xe.stats.groupCompileCount == 0)
                        xe.stats.t0 = new Date();
                    xe.parseGroups(element, attributes);
                    xe.stats.groupCompileCount++;
                    if (xe.stats.groupCompileCount == 100)
                        xe.stats.t100 = new Date() - xe.stats.t0;
                }
            }
        }
    }
};

xe.groupProcessing = function(prio, context, logging) {
    return function() {
        return {
            restrict: 'ECA',
            priority: prio,  //AngularJS ngInclude directive has 400
            link: function ($scope, element, attributes) {
                if (logging > xe.logging.none)
                    console.log('Link ', context, prio, $scope.src||attributes.src||'', '\n', logging==xe.logging.verbose?element[0].innerHTML:element);
                xe.extendPagePart(element, attributes);
            }
        }
    }
};

xe.inheritSection = function(prio, context, logging) {
    return function() {
        return {
            restrict: 'ECA',
            priority: prio,  //AngularJS ngInclude directive has 400
            compile:  function (element, attributes)  {

                if (logging > xe.logging.none)
                    console.log('Compile ', context,prio, attributes.src||'', '\n', logging==xe.logging.verbose?element[0].innerHTML:element);
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

angular.module('extensibility', [])
    .run(function(){

        if ( window.location.search.indexOf("xeLogging=debug")!=-1 )
            xe.logging.level=xe.logging.debug;
        if ( window.location.search.indexOf("xeLogging=verbose")!=-1 )
            xe.logging.level=xe.logging.verbose;
        xe.startup();

    })
    .directive('xeSection', function() {
        return {
            restrict: 'A',
            compile: function ( element, attributes /*, transclude */ ) {
                if ( xe.ng.deferedSectionTags[element[0].tagName] ) {
                    return; // Section processing to happen in a child of this tag
                }
                if (xe.logging.level > xe.logging.none)
                    console.log('Compile Section',attributes.xeSection);
                if (xe.extensions.sections[attributes.xeSection]) {
                    if (xe.logging.level > xe.logging.none)
                        console.log('Extending section ' + attributes.xeSection);
                    xe.extend(element, attributes);
                }
            }
        }
    })
    //Assume pages loaded via ng-include or ui-view start with one of the tags with groupCompile below
    //If the parentNode has xeSectionInh, treat the content as if it were a section with name xeSectionInh
    //In developer mode do some of the page parsing
    .directive( 'div', xe.groupCompile(0,'div' ,xe.logging.level))
    .directive('span', xe.groupCompile(0,'span',xe.logging.level))
    .directive('form', xe.groupCompile(0,'form',xe.logging.level))
    //
    .directive('ngInclude', xe.inheritSection (xe.ng.priorities.ngIncludeBase+1, 'before ng-include', xe.logging.level)) // before baseline include, add xeSectionInh attribute (priority > baseline ng-include)
    .directive('ngInclude', xe.groupProcessing(xe.ng.priorities.ngIncludeBase-1, 'after ng-include' , xe.logging.level))  // after  baseline include do the group processing like moving/removing sections
    .directive('uiView'   , xe.groupProcessing(  0,'ui-view'   , xe.logging.level))
    .directive('body'     , xe.groupProcessing(  0,'body'      , xe.logging.level))
    //.directive('xeAccordion' , xe.groupLink(0,'xeAccordion',true))
;