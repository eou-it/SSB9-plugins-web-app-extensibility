/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.extensibility

class ExtensionUtility {

    static String derivePageName(pageUrl) {
        // capture the last URL component either after a slash
        // or before a final slash
        def rx = /.*\/([^\/.]+)/;
        def match = pageUrl =~ rx;
        return match.groupCount() ? match[0][1] : ""
    };

    static String deriveApplicationName(pageUrl) {
        return pageUrl.substring(1,pageUrl.indexOf('/',1));
    };

}
