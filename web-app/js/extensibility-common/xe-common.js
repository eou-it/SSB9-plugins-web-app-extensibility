// xe namespace is for providing eXtensible Environment functionality.
// this will likely be provided as a service and injected
var xe = (function (xe) {
    //Attributes
    xe.typePrefix = 'xe-';                                                       //prefix for xe specific html attributes
    xe.type = {field: 'field',section: 'section'};                               //logical type names
    xe.attr = {field: xe.typePrefix+'field', section: xe.typePrefix+'section', labeledBy: 'aria-labelledby',
               hint: 'placeholder', title: 'title'};   //html attribute names
    xe.attrInh = {section: xe.typePrefix+'section-inh'};                         //html attribute name for section inherited
    xe.forTypePrefix = 'xe-for-';
    xe.errors = [];

    //Logging
    xe.logging = {none: 0, debug: 1, verbose: 2};
    xe.logging.level = xe.logging.none;
    if ( window.location.search.indexOf("xeLogging=debug")!=-1 )
        xe.logging.level=xe.logging.debug;
    if ( window.location.search.indexOf("xeLogging=verbose")!=-1 )
        xe.logging.level=xe.logging.verbose;

    xe.log = function () {
        if (xe.logging.level>xe.logging.none) {
            //probably can implement this more elegantly...
            if (arguments.length == 1)
                console.log(arguments[0]);
            if (arguments.length == 2)
                console.log(arguments[0], arguments[1]);
            if (arguments.length == 3)
                console.log(arguments[0], arguments[1], arguments[2]);
            if (arguments.length == 4)
                console.log(arguments[0], arguments[1], arguments[2], arguments[3]);
        }
    };

    //I18N - we use extensibility specific resource loading. We should investigate if we can
    //extend the baseline properties loading as done for ZK pages
    xe.loadResources = function(){
        //load resources synchronously to make sure they are available when needed.
        $.ajax({
            url: '/'+xe.page.application+'/internal/resources',
            dataType: 'json',
            data: {application: xe.page.application,page: xe.page.name,hash:location.hash},
            async: false,
            success: function(json){
                xe.log('resources loaded');
                var resources=json[0]; //data used for extending page
                for (key in resources) {
                    $.i18n.map[key] = resources[key];
                }
            }
        });
    }

    // item is a string or an object with a key attribute
    // for now we don't support args
    xe.i18n = function (item, args) {
        return (typeof item == 'string')?item: $.i18n.prop(item.key, args);
    }

    //Check if we are in developer mode
    xe.devMode = function() {
        return window.location.search.indexOf("dev=y")>-1;
        //TODO - secure by role
    }

    //This gets called several times, is it worth to refactor and eveluate URL parameters only once?
    //Don't bother for now, we may remove this from release as it is not a userstory to have enable/disable extensions
    xe.enableExtensions = function() {
        return window.location.search.indexOf("baseline=y")==-1;
    }


    // create a selector for an element - specify a name or a selector for all with a specific type
    xe.selector = function( elementType, name ) {
        if (name)
            return '['+ xe.typePrefix + elementType + '=' + name + ']';
        else
            return '['+ xe.typePrefix + elementType+']';
    }

    xe.selectorFor = function( elementType, name ) {
      return '[' + xe.forTypePrefix + elementType + (name ? '=' + name: '') + ']';
    }

    // Create a selector for removing an element and its associated labels, etc.
    xe.selectorToRemove = function( elementType, name ) {
        return xe.selector( elementType, name) + ', ' + xe.selectorFor( elementType, name ); //['+xe.typePrefix+'="' + name + '"]';
    }

    // get a simple selector for the group (well, nothing specific for a group so far)
    // might want to assure it has a child section
    xe.getGroupSelector = function(element) {
        var res = element[0].tagName;
        if (element[0].id)
            res += '#'+element[0].id;
        if (element[0].class)
            res += '.'+element[0].class;
        return res;
    }

    xe.getFields = function(section) {
        var fields = $(xe.selector(xe.type.field),section);
        var res = [];
        $.each(fields, function(i,field ){
            res.push({name:field.attributes[xe.typePrefix+xe.type.field].value, html:field.outerHTML});
        });
        return res;
    }

    xe.getPageName = function() {
        // return location.pathname.substring(location.pathname.indexOf('/ssb/')+5);
        return location.pathname.substring(location.pathname.lastIndexOf('/')+1);
    }
    xe.getApplication = function() {
        return location.pathname.substring(1,location.pathname.indexOf('/',1));
    }

    // Metadata definition for page parsing - for now just for showing as a help for extension developers
    // Basic structure (example, Sections can be nested but don't have to):
    // Page
    //      Section1
    //          Section 1.1
    //              Field 1.1.1
    //              ...
    //          Section 1.2
    //          ...

    xe.Page = function(description) {
        return {
            description: description,
            application: xe.getApplication(),
            name: xe.getPageName(),
            groups:[],
            addGroup: function (group,sections) {
                var xeType=xe.type.section;
                var res=[];
                $.each(sections, function(i,section ){
                    res.push({name:section.attributes[xe.typePrefix+xeType].value, fields:xe.getFields(section)});
                });
                this.groups.push({name: xe.getGroupSelector(group), sections: res});
            },
            // Refactoring page structure parsing for Angular
            // Leaving Groups in case it is used for JQuery pages
            //elements:[], //sections and fields are added here
            dom:{children:[]},
            level: 0,
            count: 0,
            addNode: function (node,element){
                function equ(left,right) {
                    return (left.section && left.section == right.section) || left.field && left.field == right.field;
                }
                var found=false;
                this.level++;
                this.count++;
                if (this.level>50 || this.count>100000)
                    throw ('Recursion / endless loop error '+this.level + '/' + this.count );
                if (equ(element.parent,node)) {
                    if (!node.children)
                        node.children=[];
                    node.children.push(element);

                    xe.log('Added Element',element.section || element.field, 'To child of ',node.section || node.field);
                    found = true;
                }
                //If not found, recurse children
                for (var i = 0; !found && node.children && i < node.children.length; i++) {
                    found = this.addNode(node.children[i], element);
                }
                this.level--;
                return found;
            },
            addElement: function (element) {
                //this.elements.push(element);
                if (element.parent==null)
                    this.dom.children.push(element);
                else {
                    this.addNode(this.dom, element);
                }
            }
        }
    }

    //metadata for baseline page
    xe.page = new xe.Page('Baseline Page');
    //metadata for  page after extension - just do Baseline for now
    // xe.extendedPage = new xe.Page('Extended Page');

    // xe.extensions = {} // load extensions for each page from server-side config

    // templates define how to create each type of widget

    xe.templates = {

        // should probably convert these to a string templating mechanism, ...${field.name}..., etc.
        'static': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><span title="title text" title="title text" data-xe-field="' + field.name + '">{{' + field.name + '}}</span></div>';
        },
        'literal': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><span title="title text" title="title text" data-xe-field="' + field.name + '">' + field.value + '</span></div>';
        },
        'input': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><input title="title text" data-xe-field="' + field.name + '" ng-model="' + field.name + '"></input></div>';
        },
        'text': function( field ) {
            return '<div data-xe-for="' + field.name + '"><label>' + (field.label||'Empty Label') + '</label><text title="title text" data-xe-field="' + field.name + '" ng-model="' + field.name + '"></text></div>';
        }
    }

    // utility function to generate the HTML for a specific field
    xe.generateField = function(field) {
        var template = xe.templates[field.template] || xe.templates['static'];
        xe.log( 'using template ' + field.template + '=' + template)
        return template( field );
    };

    // Apply configured extensions to an element
    xe.extend = function (element, attributes, actions) {

        function getType(param) {
            if (param.section)
                return xe.type.section;
            else if (param.field)
                return xe.type.field;
            else
                throw 'Error: section or field is required for the current operation';
        }

        function insertElementBeforeOrAfter(element,context,param) {
            var type = getType(param);
            var to;
            if (element.length==0) {
                xe.errors.push('Unable to find element. ' + JSON.stringify(param));
                return null;
            }
            if ( param.after ) {
                to = $(xe.selector(type, param.after), context);
            } else if (param.before){
                to = $(xe.selector(type, param.before), context);
            } else {
                xe.errors.push('Parameter before or after is required for operation. '+JSON.stringify(param));
                return null;
            }
            if (to.length==0) {
                xe.errors.push('Unable to find target element. '+JSON.stringify(param));
                return null;
            }
            if ( param.after ) {
                element.insertAfter(to);
            } else if (param.before){
                element.insertBefore(to);
            }
            return element;
        }

        function add(param) {
            var context = this;
            var it;
            if (param.component) {
                it = xe.renderComponent(param.component);
                xe.log('Component HTML: ',it);
            } else { //if param.html is set, use it otherwise generate from template
                it = param.html ? param.html : xe.generateField(param);
            }
            it=$(it).addClass("xe-added");
            insertElementBeforeOrAfter(it,context,param);
            xe.log('add', it);
        }

        function remove(param) {
            var element = this;
            var type = getType(param);
            var it = $(xe.selectorToRemove(type,param[type]), element);
            xe.log('remove', it);
            it.replaceWith('<span class="xe-removed" '+xe.attr[type]+'="'+param[type]+'"></span>');
        }

        function move(param) {
            var context = this;
            var type = getType(param);
            var elementToMove = $(xe.selector(type, param[type]), context).addClass("xe-moved");
            insertElementBeforeOrAfter(elementToMove,context,param);
            xe.log('move', elementToMove);
        }

        function replaceHint(element,param) {
            var type = getType(param);
            if (type != xe.type.field) {
                // not a field so not sure what to do with hint
                return
            }
            var item = $(xe.selector(type,param[type]), element );
            if (item.length > 0) {
                $(item[0]).attr(xe.attr.hint,xe.i18n(param["hint"]))
            } else {
                xe.errors.push('Unable to find and replace hint for '+param[type]);
            }
        }

        function replaceTitle(element,param) {
            var type = getType(param);
            var item = $(xe.selector(type,param[type]), element );
            if (item.length > 0) {
                $(item[0]).attr(xe.attr.title,xe.i18n(param["title"]))
            } else {
                xe.errors.push('Unable to find and replace title for '+param[type]);
            }
        }

        function replaceLabel(element,param) {
            var type = getType(param);
            var item = $(xe.selector(type,param[type]), element );
            var labelElement;

            if (item[0].attributes[xe.attr.labeledBy]){
                labelElement = $('#'+item[0].attributes[xe.attr.labeledBy].value,element);
            } else {
                //get label inside item
                labelElement = $('label', item);
            }
            if (labelElement.length) {
                labelElement[0].innerHTML = xe.i18n(param.label);
            } else {
                xe.errors.push('Unable to find and replace label for '+param[type]);
            }
        }

        function replace(param) {
            var element = this;
            if (param.html) {
                var type = getType(param);
                var it = $(xe.selectorToRemove(type,param[type]), element);
                var to = null;
                if (param.component) {
                    to = xe.renderComponent(param.component);
                } else { //if param.html is set, use it otherwise generate from template
                    to = param.html ? param.html : xe.generateField(param);
                }
                to = $(to).addClass("xe-replaced");
                xe.log('replace', it);
                it.replaceWith(to);
            } else {
                if (param.label) {
                    replaceLabel(element, param);
                }
                if (param.hint) {
                    replaceHint(element, param);
                }
                if (param.title) {
                    replaceTitle(element, param);
                }
            }
        }

        // Do nothing if extensions are not enabled
        if (!xe.enableExtensions())
            return;

        var start = new Date().getTime();
        element = element||$('body'); //Make sure element has a value

        if (actions) {
            //Group / pagePart extensions
            if (actions.move){
                [actions.move].map(move, element);
            }
            if (actions.remove){
                [actions.remove].map(remove, element);
            }
        }
        else {
            var section = attributes.xeSection || attributes.xeSectionInh;
            if (section) {
                var extensions = xe.extensions.sections[section];
                if (extensions) {
                    if (extensions.remove)
                        extensions.remove.map(remove, element);
                    if (extensions.add)
                        extensions.add.map(add, element);
                    if (extensions.move)
                        extensions.move.map(move, element);
                    if (extensions.replace)
                        extensions.replace.map(replace, element);
                }
            }
        }
        xe.log("Time to process extensions/ms: "+(new Date().getTime()-start));
    };  // end xe.extend

    //This function searches element children for sections
    xe.parseGroups = function(element,attributes){
        var sections = element.children(xe.selector(xe.type.section));
        if ( sections.length > 0) {
            xe.page.addGroup( element, sections);
            xe.log("Parsed group: ",xe.page);
        }
    };

    //This function does group level changes on a page or part of a page (ng-include, ui-view or a handlebars template)
    xe.extendPagePart = function(element,attributes){
        var xeType = xe.type.section;
        var sections = $(xe.selector(xeType),element);
        if ( sections.length > 0) {
            if (xe.enableExtensions() ) {
                $.each(sections, function(i,section ){
                    var sectionName = section.attributes[xe.typePrefix+xeType].value;
                    var actions = xe.extensions.groups.sections[sectionName];
                    if (actions)
                        xe.extend(element, attributes, actions);
                });
            }
        }
    };


    //HTML rendering support
    xe.isVoidElement = function(tag) {
        //xe.voidElements is a map to specify the html element types that are 'void' (have no content)
        if (!xe.voidElements) {
            xe.voidElements = {};
            //Initialize voidElements map
            ["area", "base", "br", "col", "command", "embed", "hr", "img", "input",
                "keygen", "link", "meta", "param", "source", "track", "wbr"].forEach(function (val, idx, array) {
                    xe.voidElements[val] = true;
                }
            );
        }
        return xe.voidElements[tag.toLowerCase()];
    }

    //Render a generic component as HTML (a component is a simplified representation of HTML DOM element)
    xe.renderComponent = function ( component ) {
        //Note: void elements don't need </tag> and have no content
        var result="";
        if (component.tagName) {
            result="<"+component.tagName;
            Object.getOwnPropertyNames(component.attributes).forEach(function(val,idx,array) {
                    result+=' '+val+'="'+component.attributes[val]+'"';
                }
            );
            result += ">";
            //recursively add the child nodes
            if (component.childNodes && !xe.isVoidElement(component.tagName) ) {
                component.childNodes.forEach(function (val, idx, array) {
                        result += ' ' + xe.renderComponent(val);
                    }
                );
            }
        }
        if (component.textContent && (!component.tagName || !xe.isVoidElement(component.tagName))) {
            result += component.textContent;
        }
        if (component.tagName && !xe.isVoidElement(component.tagName)){
            result += "</" + component.tagName + ">";
        }
        return result;
    }


    // Page Editor - well, just show for now just show a simple node hierarchy
    xe.renderPageStructureNew = function(page){
        var count = 0;
        function renderNode(node, html) {
            count++;
            if (count>10000)
                throw ('Recursion error');
            if (node.section) {
                html += "<li>Section: " + node.section + "</li>\n";
            }
            else if (node.field) {
                html += "<li>Field: " + node.field + "</li>\n";
            }
            if (node.children) {
                html+="<ul>";
                for (var i = 0; i < node.children.length; i++) {
                    html  = renderNode(node.children[i], html);
                }
                html+="</ul>";
            }
            return html;
        }

        var result = "Page Structure "+page.description+" "+page.application+"/"+page.name;
        result = renderNode(page.dom, result);
        return result;
    }

    // If not used by anyone, remove this
    xe.renderPageStructure = function(page) {
        var result = "Page Structure "+page.description+" "+page.application+"/"+page.name;

        var renderFields = function(fields) {
            var res = "<ul>";
            fields.forEach(
                function (field,idx,array){
                    res += "<li>Field: "  +field.name+"\n";
                    res += "</li>";
                }
            );
            res+="</ul>";
            return res;
        }

        var renderSections = function(sections) {
            var res = "<ul>";
            sections.forEach(
                function (section,idx,array){
                    res += "<li>Section: "+section.name+"\n";
                    res += renderFields(section.fields);
                    res += "</li>";
                }
            );
            res+="</ul>";
            return res;
        }

        var renderGroups = function(groups) {
            var res = "<ul>";
            groups.forEach(
                function (group,idx,array){
                    res += "<li >Group "+group.name+"\n";    // have no name really
                    res += renderSections(group.sections);
                    res += "</li>";
                }
            );
            res+="</ul>";
            return res;
        }
        result += renderGroups(page.groups);
        return result;
    }

    xe.popups = [null,null,null];


    xe.showPageStructure = function (page, popup){
        if (!popup) {
            popup = $('<div id="pageStructure'+page.description+'" ></div>');
            popup.dialog({appendTo: "#content", width: 600, height:"auto"});
            popup.append(xe.renderPageStructureNew(page));
        }
        popup.dialog("open");
        return popup;
    }

    xe.stats = {};

    xe.showStats = function (page, popup) {
        xe.renderStats = function(page){
            var res=result = "Page Statistics "+page.name;;
            for (key in xe.stats) {
                res += '<li>'+key+'='+xe.stats[key];
            }
            if (xe.errors && xe.errors.length)
                res+='<br>Errors<br>'+xe.errors;
            return res;
        }

        if (!popup) {
            popup = $('<div id="pageStats' + page.name + '" ></div>');
            popup.dialog({appendTo: "#content", width: 600, height: "auto"});
            popup.append(xe.renderStats(page));
        }
        popup.dialog("open");
        return popup;
    }

    xe.extensionsEditor = function(page,popup) {
        if (!popup) {
            popup = $('<div id="extensionsEditor' + page.name + '" ></div>');
            popup.dialog({appendTo: "#content", width: 600, height: "auto"});
            popup.load(extensibilityPluginPath+'/templates/extedit.html',
                       function(x){
                           $('#extensions-edit-input',popup).text(JSON.stringify(xe.page.metadata,null,2));
                       }
            );
        }
        popup.dialog("open");
        return popup;
    }

    //Post updated extensions to the database
    xe.saveExtensions=function(){
        //var md = JSON.parse(xe.page.metadata);
        var data={application: xe.page.application, page:xe.page.name, metadata: xe.page.metadata } ;
        $.ajax({
            url: '/' + xe.page.application + '/internal/extensions',
            type:'POST',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            processData: true,
            success: function(data){
                        alert('Saved');
                        xe.log('Data Saved',data);
                     }
        });
    }

    //Update the model with modifed extensions
    xe.setExtensions = function (value) {
        xe.page.metadata = JSON.parse(value);
    }

    //Add the tools menu item Extensibility if we are in developer mode
    xe.addExtensibilityMenu = function () {
        if (xe.devMode()) {
            ToolsMenu.addSection("extensibility", "Extensibility");
            ToolsMenu.addItem("pagestructurebase", "Show Baseline Page Structure", "extensibility", function () {
                xe.popups[0] = xe.showPageStructure(xe.page, xe.popups[0])
            });
            ToolsMenu.addItem("pagestats", "Show Page Statistics / Status", "extensibility", function () {
                xe.popups[1] = xe.showStats(xe.page, xe.popups[1])
            });
            ToolsMenu.addItem("extensionseditor", "Edit Extensions", "extensibility", function () {
                xe.popups[2] = xe.extensionsEditor(xe.page, xe.popups[2])
            });
        }
    }

    xe.startup = function(){

        var normalizeMetadata = function(){
            //add actions per section to group so actions can be directly accessed for a section
            xe.extensions.groups.sections = {};
            xe.extensions.groups.remove.forEach( function(val) {
                if (!xe.extensions.groups.sections[val.section])
                    xe.extensions.groups.sections[val.section] = {};
                xe.extensions.groups.sections[val.section].remove = val;
            });
            xe.extensions.groups.move.forEach( function(val){
                if (!xe.extensions.groups.sections[val.section])
                    xe.extensions.groups.sections[val.section] = {};
                xe.extensions.groups.sections[val.section].move = val;
            });

            // reorder positioning metadata taking account of any dependencies
            reorderMetadata();
        };

        /***************************************************************************************************
        item positioning information relies on the correct order of repositioning due to dependencies
        reorder section and group positioning meta data accordingly
        ***************************************************************************************************/
        var reorderMetadata = function() {

            // process each group in turn
            reorder( xe.extensions.groups, "section" );

            // process each section in turn
            _.each( xe.extensions.sections, function(section) {
                reorder( section, "field" );
            });
        };

        /***************************************************************************************************
        reorder positioning metadata
         ***************************************************************************************************/
        var reorder = function( container, key ) {

            var orderedMoves = [];
            var allExtensionsApplied = false;
            var extension;

            // mark all item positioning extensions as currently unprocessed
            _.each( container.move, function(n) {
                n.processed = false;
            });

            while ( !allExtensionsApplied ) {

                // find an unapplied extension
                extension = _.find( container.move, function(n){ return n.processed == false });

                if ( extension ) {

                    extension = getDependency( extension, container.move, key );
                    orderedMoves.push( extension );
                    extension.processed = true;

                } else {
                    allExtensionsApplied = true;
                }
            }
            container.move = orderedMoves;
        }

        /***************************************************************************************************

         Some item moves need to take place before others eg.

         {"field": "courseReferenceNumber", "before": "term"},
         {"field": "term", "before": "subject"},
         {"field": "subject", "before": "gradedStatus"}

         subject needs to be moved first as term is dependent on position of subject.
         term needs to be moved next as courseReferenceNumber is dependent on position of term
         etc

         This routine determines the next move operation that needs to take place

         ***************************************************************************************************/
        var getDependency = function( extension, extensions, key ){

            var complete = false;
            var dependency;

            while ( !complete ) {

                dependency = _.find( extensions, function(n){
                    return (n[key] == extension.before) && n.processed == false;
                });

                if (!dependency) {
                    complete = true;
                }
                else {
                    extension = dependency;
                }
            }
            return extension;
        }

        xe.log('Startup - fetching metadata...');
        //load meta-data synchronously to make sure it is available before compile needs it.
        $.ajax({
            url: '/'+xe.page.application+'/internal/extensions',
            dataType: 'json',
            data: {application: xe.page.application,page: xe.page.name,hash:location.hash},
            async: false,
            success: function(json){
                xe.log('data loaded');
                xe.extensions=json[0]; //data used for extending page
                if (xe.devMode()){
                    xe.page.metadata=[$.extend(true,{},xe.extensions)];  //clone of extensions used for editor
                }
                normalizeMetadata();
            }
        });
        xe.log(xe.extensions);
        xe.loadResources();
        xe.addExtensibilityMenu();
    }

    $(xe.startup);

    return xe;
})(xe || {});
