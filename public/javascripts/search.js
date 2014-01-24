function applySearch(contextSearch, contextTo) {
	
    var resultMap = {};

	$('#search-query').typeahead({
		remote: {
			url: contextSearch + '/filterbyjson/%QUERY',
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
						url: item.url
					};
				}				
	          	return searchResults;
			}
		}
	}).on('typeahead:selected', function(event, datum) {
		console.log("contextTo: " + contextTo);
		if (contextTo !== undefined) {
			document.location = contextTo + "/" + datum.url; 
		} else {
			document.location = contextSearch + "/" + datum.url; 
		}
	});
}
