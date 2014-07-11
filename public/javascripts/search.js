function applySearchTargetsTab(context, searchContext, urlTo) {

	if (searchContext !== undefined) {
	    var resultMap = {};
		$('#search-query').typeahead({
			remote: {
				url: context + searchContext + '/filterbyjson/%QUERY',
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
							field_url: item.field_url
						};
					}				
		          	return searchResults;
				}
			},
			 template: '<p><strong>{{value}}</strong></p><p>{{field_url}}</p>',
			 engine: Hogan
		}).on('typeahead:selected', function(event, datum) {
			console.log("contextTo: " + urlTo);
			if (datum.field_url !== undefined) {
				$(this).val(datum.field_url);
			}
			if (urlTo !== undefined) {
				window.location.replace(context + urlTo + "/" + "view?target=" + datum.url + "&tabStatus=overview");
			} else {
				window.location.replace(context + searchContext + "/" + "view?target=" + datum.url); 
			}
		});
	}
}

function scopeCheck(context) {
    var idle_timeout,
    SCOPE_URI = context + 'api/scope/',
    MIN_TEXT_LENGTH = 4, // minimum length annotation must have before being allowed to the doScope server
    TRIGGER_CHARS = ". ,", // characters that force an doScope lookup
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
			    	console.log("success " + data);
	    		} else {
			    	addButton.css('background-color','red');
			    	searchButton.css('background-color', 'red');
			    	console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	addButton.css('background-color','red');
		    	searchButton.css('background-color', 'red');
		    	console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
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
    LICENCE_URI = context + 'api/licence/',
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
			    	saveButton.css('background-color','red');
			    	console.log("success " + data);
	    		} else {
			    	saveButton.css('background-color','green');
			    	console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	saveButton.css('background-color','red');
		    	console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
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
    LICENCE_URI = context + 'api/licencelevel/',
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
			    	console.log("success " + data);
	    		} else {
			    	saveButton.css('background-color','green');
			    	console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	saveButton.css('background-color','yellow');
		    	console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
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
			    	console.log("success " + data);
	    		} else {
			    	saveButton.css('color','green');
//			    	saveButton.css('background-color','green');
			    	console.log("success " + data);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
		    	saveButton.css('background-color','green');
		    	console.log("error " + jqXHR.status + " " + textStatus + " " + errorThrown);
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

function applySearchTargets(context, searchContext, urlTo) {
	if (searchContext !== undefined) {
	    var resultMap = {};
		$('#search-query').typeahead({
			remote: {
				url: context + searchContext + '/filterbyjson/%QUERY',
				filter: function(items) {
					var searchResults = [];
					for (var i = 0; i < items.length; i++) {
						var item = items[i];
						label = item.field_url;
						searchResults[i] = {
							value: label,
							title: item.title
						};
					}				
		          	return searchResults;
				}
			},
			 template: '<p><strong>{{title}}</strong></p><p>{{value}}</p>',
			 engine: Hogan
		}).on('typeahead:selected', function(event, datum) {
			window.location = datum.value;
		});
	}
}

function applySearch(context, searchContext, urlTo) {

	if (searchContext !== undefined) {
	    var resultMap = {};
		$('#search-query').typeahead({
			remote: {
				url: context + searchContext + '/filterbyjson/%QUERY',
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
							field_url: item.field_url
						};
					}				
		          	return searchResults;
				}
			},
			 template: '<p><strong>{{value}}</strong></p><p>{{field_url}}</p>',
			 engine: Hogan
		}).on('typeahead:selected', function(event, datum) {
			console.log("contextTo: " + urlTo);
			if (datum.field_url !== undefined) {
				$(this).val(datum.field_url);
			}
			if (urlTo !== undefined) {
				window.location.replace(context + urlTo + "/" + datum.url);
			} else {
				window.location.replace(context + searchContext + "/" + datum.url); 
			}
		});
	}
}


function applySearchExt(context, searchContext, urlTo) {

	if (searchContext !== undefined) {
	    var resultMap = {};
		$('#search-query').typeahead({
			remote: {
				url: context + searchContext + '/filterbyjson/%QUERY',
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
							field_url: item.field_url
						};
					}				
		          	return searchResults;
				}
			},
			 template: '<p><strong>{{value}}</strong></p><p>{{field_url}}</p>',
			 engine: Hogan
		}).on('typeahead:selected', function(event, datum) {
			console.log("contextTo: " + urlTo);
			if (datum.field_url !== undefined) {
				$(this).val(datum.field_url);
			}
			if (urlTo !== undefined) {
				window.location.replace(context + urlTo + "/view?target=" + datum.url);
			} else {
				window.location.replace(context + searchContext + "/view?target=" + datum.url); 
			}
		});
	}
}

