// jquery/backbone-specific extensibility code
xe.jq = (function(xe) {
  var jq = xe.jq || {};

  jq.extendSection = function(sectionElement) {
        xe.extend(sectionElement, {xeSection:$(sectionElement).data('xe-section') } );
  };

  jq.extend = function(rootElement) {
    $(xe.selector('section'), rootElement).each( function(idx, ele) {jq.extendSection(ele);} );
  }

  $( function() {
    xe.jq.extend();
  });

  return jq;
})(xe||{});
