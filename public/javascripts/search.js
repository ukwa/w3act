function applySearchTargetsTab(context, searchContext, urlTo) {

	if (searchContext !== undefined) {
	    var resultMap = {};
		$('#search-query').typeahead({
			remote: {
				url: context + '/' + searchContext + '/filterbyjson/%QUERY',
				filter: function(items) {
					var searchResults = [];
					for (var i = 0; i < items.length; i++) {
						var item = items[i];
						var label = item.name;
						if (label === undefined) {
							label = item.title;
						}
						var urls = "";
						var fieldUrls = item.fieldUrls;
						for (var x = 0; x < fieldUrls.length; x++) {
							urls += fieldUrls[x].url + " ";
						}
						searchResults[i] = {
							value: label,
							url: item.url,
							field_url: urls,
							id: item.id
						};
					}				
		          	return searchResults;
				}
			},
			 template: '<p><strong>{{value}}</strong></p><p>{{field_url}}</p>',
			 engine: Hogan
		}).on('typeahead:selected', function(event, datum) {
			//console.log("contextTo: " + urlTo + " " + datum.id);
			if (datum.field_url !== undefined) {
				$(this).val(datum.field_url);
			}
			if (urlTo !== undefined) {
				window.location.href = context + "/" + urlTo + "/" + datum.id + "&tabStatus=overview";
			} else {
				window.location.href = context + "/" + searchContext + "/" + datum.id;
			}
		});
	}
}

function scopeCheck(context) {
    var idle_timeout,
    SCOPE_URI = context + '/api/scope/',
    MIN_TEXT_LENGTH = 4, // minimum length annotation must have before being allowed to the doScope server
    TRIGGER_CHARS = ". ,;", // characters that force an doScope lookup
    IDLE_THRESHOLD = 2000; // doScope is also done after IDLE_THRESHOLD milliseconds of key idleness

	addButton = $('#addentry')
	searchButton = $('#search')

    // Does the Scope lookup
    var doScope = function(text) {
	    $.ajax({
	    	url: SCOPE_URI + text,
	    	dataType: 'json',
	    	success: function(data) {
	    		if (data) {
			    	addButton.css('background-color','green');
			    	searchButton.css('background-color', 'green');
			    	//console.log("success " + data);
	    		} else {
			    	addButton.css('background-color','red');
			    	searchButton.css('background-color', 'red');
			    	//console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	addButton.css('background-color','red');
		    	searchButton.css('background-color', 'red');
		    	//console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
	    	}
	    });
    };
    
    // Restarts the keyboard-idleness timeout
    var restartIdleTimeout = function(text) {
	    if (idle_timeout) {
		    window.clearTimeout(idle_timeout);
	    }
	    idle_timeout = window.setTimeout(function() { 
	    	doScope(text); 
	    }, IDLE_THRESHOLD);
    };                
   
    $("#search-query").keyup(function() {
    	var text = $(this).val();

    	if (text.length > MIN_TEXT_LENGTH) {
    		restartIdleTimeout(text);
           
            if (TRIGGER_CHARS.indexOf(text[text.length - 1]) > -1)
	            doScope(text);
            }
    });
}

function licenceCheck(context) {
    var idle_timeout,
    LICENCE_URI = context + '/api/licence/',
    MIN_TEXT_LENGTH = 4, // minimum length annotation must have before being allowed to the doLicence server
    TRIGGER_CHARS = ". ,;", // characters that force an doScope lookup
    IDLE_THRESHOLD = 2000; // doLicence is also done after IDLE_THRESHOLD milliseconds of key idleness

	saveButton = $('#save')

    // Does the Licence lookup
    var doLicence = function(text) {
	    $.ajax({
	    	url: LICENCE_URI + text,
	    	dataType: 'json',
	    	success: function(data) {
	    		if (data) {
			    	saveButton.css('background-color','red');
			    	//console.log("success " + data);
	    		} else {
			    	saveButton.css('background-color','green');
			    	//console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	saveButton.css('background-color','red');
		    	//console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
	    	}
	    });
    };
    
    // Restarts the keyboard-idleness timeout
    var restartIdleTimeout = function(text) {
	    if (idle_timeout) {
		    window.clearTimeout(idle_timeout);
	    }
	    idle_timeout = window.setTimeout(function() { 
	    	doLicence(text); 
	    }, IDLE_THRESHOLD);
    };                
   
    $("#search-query").keyup(function() {
    	var text = $(this).val();

    	if (text.length > MIN_TEXT_LENGTH) {
    		restartIdleTimeout(text);
           
            if (TRIGGER_CHARS.indexOf(text[text.length - 1]) > -1)
	            doLicence(text);
            }
    });
}

function licenceHigherLevelCheck(context) {
    var idle_timeout,
    LICENCE_URI = context + '/api/licencelevel/',
    MIN_TEXT_LENGTH = 4, // minimum length annotation must have before being allowed to the doLicence server
    TRIGGER_CHARS = ". ,", // characters that force an doScope lookup
    IDLE_THRESHOLD = 2000; // doLicence is also done after IDLE_THRESHOLD milliseconds of key idleness

	saveButton = $('#save')

    // Does the Licence lookup
    var doLicence = function(text) {
	    $.ajax({
	    	url: LICENCE_URI + text,
	    	dataType: 'json',
	    	success: function(data) {
	    		if (data) {
			    	saveButton.css('background-color','yellow');
			    	//console.log("success " + data);
	    		} else {
			    	saveButton.css('background-color','green');
			    	//console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	saveButton.css('background-color','yellow');
		    	//console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
	    	}
	    });
    };
    
    // Restarts the keyboard-idleness timeout
    var restartIdleTimeout = function(text) {
	    if (idle_timeout) {
		    window.clearTimeout(idle_timeout);
	    }
	    idle_timeout = window.setTimeout(function() { 
	    	doLicence(text); 
	    }, IDLE_THRESHOLD);
    };                
   
    $("#search-query").keyup(function() {
    	var text = $(this).val();

    	if (text.length > MIN_TEXT_LENGTH) {
    		restartIdleTimeout(text);
           
            if (TRIGGER_CHARS.indexOf(text[text.length - 1]) > -1)
	            doLicence(text);
            }
    });
}

function licencePromptHigherLevel(context) {
    var idle_timeout,
    LICENCE_URI = context + 'api/licenceprompthigherlevel/',
    MIN_TEXT_LENGTH = 4, // minimum length annotation must have before being allowed to the doLicence server
    TRIGGER_CHARS = ". ,", // characters that force an doScope lookup
    IDLE_THRESHOLD = 2000; // doLicence is also done after IDLE_THRESHOLD milliseconds of key idleness

	saveButton = $('#filter')

    // Does the Licence lookup
    var doLicence = function(text) {
	    $.ajax({
	    	url: LICENCE_URI + text,
	    	dataType: 'json',
	    	success: function(data) {
	    		if (data) {
			    	saveButton.css('color','yellow');
//			    	saveButton.css('background-color','yellow');
			    	//console.log("success " + data);
	    		} else {
			    	saveButton.css('color','green');
//			    	saveButton.css('background-color','green');
			    	//console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	saveButton.css('background-color','green');
		    	//console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
	    	}
	    });
    };
    
    // Restarts the keyboard-idleness timeout
    var restartIdleTimeout = function(text) {
	    if (idle_timeout) {
		    window.clearTimeout(idle_timeout);
	    }
	    idle_timeout = window.setTimeout(function() { 
	    	doLicence(text); 
	    }, IDLE_THRESHOLD);
    };                
   
    $("#search-query").keyup(function() {
    	var text = $(this).val();

    	if (text.length > MIN_TEXT_LENGTH) {
    		restartIdleTimeout(text);
           
            if (TRIGGER_CHARS.indexOf(text[text.length - 1]) > -1)
	            doLicence(text);
            }
    });
}

function applySearch(context, searchContext, urlTo) {

	if (searchContext !== undefined) {
	    var resultMap = {};
		$('#search-query').typeahead({
			remote: {
				url: context + "/" + searchContext + '/filterbyjson/%QUERY',
				filter: function(items) {
					var searchResults = [];
					for (var i = 0; i < items.length; i++) {
						var item = items[i];
						var label = item.name;
						if (label === undefined) {
							label = item.title;
						}
						var urls = "";
						var fieldUrls = item.fieldUrls;
						if (searchContext == 'instances') {
							fieldUrls = item.target.fieldUrls;
						}
						if (fieldUrls != null && (searchContext == 'instances' || searchContext == 'targets')) {
	 						for (var x = 0; x < fieldUrls.length; x++) {
								urls += fieldUrls[x].url + " ";
							}
							searchResults[i] = {
									value: label,
									url: item.url,
									field_url: urls,
									id: item.id
							};
						} else {
							searchResults[i] = {
									value: label,
									url: item.url,
									id: item.id
							};
						}
					}				
		          	return searchResults;
				}
			},
			 template: '<p><strong>{{value}}</strong></p><p>{{field_url}}</p>',
			 engine: Hogan
		}).on('typeahead:selected', function(event, datum) {
			//console.log("contextTo: " + urlTo);
			if (datum.field_url !== undefined) {
				$(this).val(datum.field_url);
			}
			if (urlTo !== undefined) {
				window.location.href = context + "/" + urlTo + "/" + datum.id;
			} else {
				window.location.href = context + "/" + searchContext + "/" + datum.id;
				
			}
		});
	}
}


function applySearchExt(context, searchContext, urlTo) {

	if (searchContext !== undefined) {
	    var resultMap = {};
		$('#search-query').typeahead({
			remote: {
				url: context + "/" + searchContext + '/filterbyjson/%QUERY',
				filter: function(items) {
					var searchResults = [];
					for (var i = 0; i < items.length; i++) {
						var item = items[i];
						var label = item.name;
						if (label === undefined) {
							label = item.title;
						}
						searchResults[i] = {
							value: label,
							url: item.url,
							field_url: item.field_url,
							id: item.id
						};
					}				
		          	return searchResults;
				}
			},
			 template: '<p><strong>{{value}}</strong></p><p>{{field_url}}</p>',
			 engine: Hogan
		}).on('typeahead:selected', function(event, datum) {
			//console.log("contextTo: " + urlTo);
			if (datum.field_url !== undefined) {
				$(this).val(datum.field_url);
			}
			if (urlTo !== undefined) {
				window.location.href = context + "/" + urlTo + "/" + datum.id;
			} else {
				window.location.href = context + "/" + searchContext + "/" + datum.id;
			}
		});
	}
}

function showTree(data, id, key) {
	showTree(data, id, key, 3);
}

function showTree(data, id, key, sm) {
    var expandedNodes = [];
	var selectionMode = parseInt(sm);
 	$(id).dynatree({
		checkbox: true,
    	selectMode: selectionMode,
        autoCollapse:false,
        activeVisible: true,
    	children: data,
    	onSelect: function(select, node) {
      		// Get a list of all selected nodes, and convert to a key array:
      		var selKeys = $.map(node.tree.getSelectedNodes(), function(node){
        		return node.data.key;
      		});
      		document.getElementById(key).value = selKeys.join(",");
      		
      		//console.log("test: " + document.getElementById(key).value);
      		// Get a list of all selected TOP nodes
      		var selRootNodes = node.tree.getSelectedNodes(true);
      		// ... and convert to a key array:
      		var selRootKeys = $.map(selRootNodes, function(node){
        		return node.data.key;
      		});
    	},
        onCustomRender: function(node) {
            return "<a href=" + node.data.url + ">" + node.data.title + "</a>";
        },
    	onPostInit: function(isReloading, isError) {
            var tree = $(id).dynatree('getTree');
            var selKeys = $.map(tree.getSelectedNodes(), function(node){
                node.makeVisible();
            });
         },
 	});
}

function showTreeSelect(data, id, key) {
 	$(id).dynatree({
    	checkbox: false,
    	selectMode: 3,
    	children: data,
	    onActivate: function(node) { 
	        if( node.data.url ){
	            // use href to change the current frame:
	            window.location.href = node.data.url; 
	        }
	    },
        onCustomRender: function(node) {
            return "<a href=" + node.data.url + ">" + node.data.title + "</a>";
        },
    	onSelect: function(select, node) {
      		// Get a list of all selected nodes, and convert to a key array:
      		var selKeys = $.map(node.tree.getSelectedNodes(), function(node){
        		return node.data.key;
      		});
      		document.getElementById(key).value = selKeys.join(",");
      		// Get a list of all selected TOP nodes
      		var selRootNodes = node.tree.getSelectedNodes(true);
      		// ... and convert to a key array:
      		var selRootKeys = $.map(selRootNodes, function(node){
        		return node.data.key;
      		});
    	}
 	});
}

function showTreeParent(data, id, key) {
 	
    $(id).dynatree({
        checkbox: true,
        // Override class name for checkbox icon:
        classNames: {checkbox: "dynatree-radio"},
        selectMode: 1,
        children: data,
        onSelect: function(select, node) {
      		var selKeys = $.map(node.tree.getSelectedNodes(), function(node){
        		return node.data.key;
      		});
      		document.getElementById(key).value = selKeys.join(",");
        }
        // The following options are only required, if we have more than one tree on one page:
//        initId: "treeData",
/* 			        cookieId: "dynatree-Cb1",
        idPrefix: "dynatree-Cb1-"
*/
    });
}

function targetDateTime() {
	
	$('#start-date-time').datetimepicker(
		{ dateFormat: "dd-mm-yy" }
	);

	$('#end-date-time').datetimepicker(
		{ dateFormat: "dd-mm-yy" }
	);

	$("#crawlFrequency").change(function(event) {
		var frequency = $(this).val();
		event.preventDefault();
		if (frequency != 'DOMAINCRAWL') {
    		//console.log('selected: ' + frequency);
            var d = new Date(); // for now
            var datetext = (d.getDate()+1) + "-" + (d.getMonth()+1) + "-" + d.getFullYear() + " 09:00";
            $('#start-date-time').val(datetext);
            $('#start-date-time').attr('readonly','readonly');
		} else {
    		$("#start-date-time").val('');
		}
	});			
}