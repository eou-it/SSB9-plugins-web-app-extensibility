// xe namespace is for providing eXtensible Environment functionality.
// this will likely be provided as a service and injected
var xe = (function (xe) {
    //prefix used in html decoration for extensibility
    xe.typePrefix = 'data-xe-';

    //Check if we are in developer mode
    xe.devMode = function() {
        return window.location.search.indexOf("dev=y")>-1;
        //TODO - secure by role
    }
    // create a selector for an element
    xe.selector = function( elementType, name ) {
        if (name)
            return '['+ xe.typePrefix + elementType + '=' + name + ']';
        else
            return '['+ xe.typePrefix + elementType+']';
    }

    // create a selector for removing an element and its associated labels, etc.
    xe.selectorToRemove = function( elementType, name ) {
        return xe.selector( elementType, name) + ', ['+xe.typePrefix+'="' + name + '"]';
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
        var xeType = 'field';
        var fields = $(xe.selector(xeType),section);
        var res = [];
        $.each(fields, function(i,field ){
            res.push({name:field.attributes[xe.typePrefix+xeType].value, html:field.outerHTML});
        });
        return res;
    }

    xe.getPageName = function() {
        return location.pathname.substring(location.pathname.indexOf('/ssb/')+5);
    }
    xe.getApplication = function() {
        return location.pathname.substring(1,location.pathname.indexOf('/',1));
    }
    // Metadata definition for page parsing - for now just for showing as a help for extension developers
    // Basic structure:
    // Page
    //      Group 1
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
                var xeType='section';
                var res=[];
                $.each(sections, function(i,section ){
                    res.push({name:section.attributes[xe.typePrefix+xeType].value, fields:xe.getFields(section)});
                });
                this.groups.push({name: xe.getGroupSelector(group), sections: res});
            }
        }
    }

    //metadata for baseline page
    xe.page = new xe.Page('Baseline Page');
    //metadata for  page after extension - just do Baseline for now
    //xe.extendedPage = new xe.Page('Extended Page');

    // xe.extensions = {} // load extensions for each page from server-side config

    // templates define how to create each type of widget
    //Note HvT:
    //If we use templates, it is not possible to insert fields having the same structure as the existing application fields,
    //unless the templates are configurable too
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
        console.log( 'using template ' + field.template + '=' + template)
        return template( field );
    };

    // Apply configured extensions to an element
    xe.extend = function (element, attributes, selector) {

        function getType(param) {
            if (param.section)
                return 'section';
            else if (param.field)
                return 'field';
            else
                throw 'Error: section or field is required for the current operation';
        }

        function add(param) {
            var element = this;
            var type = getType(param);
            var it;
            if (param.component) {
                it = xe.renderComponent(param.component);
                console.log('Component HTML: ',it);
            } else { //if param.html is set, use it otherwise generate from template
                it = param.html ? param.html : xe.generateField(param);
            }
            it=$(it).addClass("xe-added");
            if ( param.after ) {
                it.insertAfter( $(xe.selector(type, param.after), element));
            } else if (param.before){
                it.insertBefore( $(xe.selector(type, param.before), element));
            } else {
                it.prependTo( element );
            }
            console.log('add', it);
        }

        function remove(param) {
            var element = this;
            var type = getType(param);
            var it = $(xe.selectorToRemove(type,param[type]), element);
            console.log('remove', it);
            it.replaceWith('<span class="xe-removed"></span>');
        }

        function move(param) {
            var element = this;
            var type = getType(param);
            var elementToMove = $(xe.selector(type, param[type], element)).addClass("xe-moved");
            if ( param.after ) {
                $(elementToMove).insertAfter( $(xe.selector(type, param.after), element));
            } else if ( param.before ) {
                $(elementToMove).insertBefore( $(xe.selector(type, param.before), element));
            } else { // move to first field in parent
            }
            console.log('move', elementToMove);
        }

        function replace(param) {
            var element = this;
            var type = getType(param);
            var it = $(xe.selectorToRemove(type,param[type]), element);
            var to = null;
            if (param.component) {
                to = xe.renderComponent(param.component);
            } else { //if param.html is set, use it otherwise generate from template
                to = param.html ? param.html : xe.generateField(param);
            }
            to=$(to).addClass("xe-replaced");
            console.log('replace', it);
            it.replaceWith(to);
        }


        var start = new Date().getTime();
        if (selector) {
            if (xe.extensions.groups[selector]) {
                if (xe.extensions.groups[selector].move){
                    xe.extensions.groups[selector].move.map(move, element);
                }
                if (xe.extensions.groups[selector].remove){
                    xe.extensions.groups[selector].remove.map(remove, element);
                }
            }
        }
        else if (attributes.xeSection) {
            if (xe.extensions.sections[attributes.xeSection]) {
                if (xe.extensions.sections[attributes.xeSection].remove)
                    xe.extensions.sections[attributes.xeSection].remove.map(remove, element);
                if (xe.extensions.sections[attributes.xeSection].add)
                    xe.extensions.sections[attributes.xeSection].add.map(add, element);
                if (xe.extensions.sections[attributes.xeSection].move)
                    xe.extensions.sections[attributes.xeSection].move.map(move, element);
                if (xe.extensions.sections[attributes.xeSection].replace)
                    xe.extensions.sections[attributes.xeSection].replace.map(replace, element);
            }
        }
        console.log("Time to process extensions/ms: "+(new Date().getTime()-start));
    };  // end xe.extend

    //This function searches element children for sections and matchs the element to a selector for groups in metadata.
    //a group selector selects the parent element of sections to move or remove
    xe.extendGroups = function(element,attributes){
        //It seems rather inefficient having to do this for all divs and maybe other elements
        var sections = element.children(xe.selector('section'));
        if (sections.length > 0) {
            if (xe.devMode()) {
                xe.page.addGroup( element, sections);
                console.log("Parsed group:",xe.page);
            }
            if (window.location.search.indexOf("baseline=y")==-1) {
                for (selector in xe.extensions.groups) {
                    var search = $(selector, element.context.parent);
                    if (search[0] == element[0]) {
                        console.log('Found match for selector ' + selector, search);
                        xe.extend(element, attributes, selector);
                    }
                }
            }
        }
    };


    //HTML rendering support
    xe.isVoidElement = function(tag) {
        //xe.voidElements is a map to specify the html element types that are 'void' (have no content)
        if (!xe.voidElements) {
            console.log('initialize void elements');
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


    // Page Editor - well, just show for now
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
            popup.dialog({appendTo: "#content", width: 600, hight:"auto"});
            popup.append(xe.renderPageStructure(page));
            //var text = JSON.stringify(xe.page,null,2);
            //xe.popup.append( $('<pre>').text(text));
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
            return res;
        }

        if (!popup) {
            popup = $('<div id="pageStats' + page.name + '" ></div>');
            popup.dialog({appendTo: "#content", width: 600, hight: "auto"});
            popup.append(xe.renderStats(page));
        }
        popup.dialog("open");
        return popup;
    }

    xe.extensionsEditor = function(page,popup) {
        if (!popup) {
            popup = $('<div id="extensionsEditor' + page.name + '" ></div>');
            popup.dialog({appendTo: "#content", width: 600, hight: "auto"});
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
        var md = JSON.parse(xe.page.metadata);
        var data={application: xe.page.application, page:xe.page.name, metadata:md  } ;
        $.ajax({
            url: '/' + xe.page.application + '/internal/extensions',
            type:'POST',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            processData: true,
            success: function() {alert('Saved');}
        });
    }

    //Update the model with modifed extensions
    xe.setExtensions = function (value) {
        xe.page.metadata = value;
    }

    //TODO Only add menu if role is adequate
    xe.addExtensibilityMenu = function () {
        ToolsMenu.addSection("extensibility", "Extensibility") ;
        ToolsMenu.addItem("pagestructurebase", "Show Baseline Page Structure", "extensibility", function() {xe.popups[0]=xe.showPageStructure(xe.page,xe.popups[0])});
        ToolsMenu.addItem("pagestats", "Show Page Statistics", "extensibility", function() {xe.popups[1]=xe.showStats(xe.page,xe.popups[1])});
        ToolsMenu.addItem("extensionseditor", "Edit Extensions", "extensibility", function() {xe.popups[2]=xe.extensionsEditor(xe.page,xe.popups[2])});
    }

    xe.startup = function(){
        console.log('Running - fetching metadata...');
        //load meta-data synchronously to make sure it is available before compile needs it.
        //TODO: do not hard code application and /ssb/ here to avoid being application and Banner specific
        $.ajax({
            url: '/'+xe.page.application+'/internal/extensions',
            dataType: 'json',
            data: {application: xe.page.application,page: xe.page.name,hash:location.hash},
            async: false,
            success: function(json){
                console.log('data loaded');
                if (xe.devMode()){
                    xe.page.metadata=json;
                }
                xe.extensions={sections:{}, groups:{}};
                for (var i=0;i<json.length; i++) {
                    //convert from array to map with key is section and value the modifications
                    if (json[i].section)
                        xe.extensions.sections[json[i].section] = json[i].modifications;
                    else if (json[i].selector)
                        xe.extensions.groups[json[i].selector] = json[i].modifications;
                }
            }
        });
        console.log(xe.extensions);
        xe.addExtensibilityMenu();

    }

    return xe;
})(xe || {});