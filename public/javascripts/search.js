function applySearch(context) {
	
    var resultMap = {};

	$('#search-query').typeahead({
		remote: {
			url: context + '/filterbyjson/%QUERY',
			filter: function(data) {
				var searchResults = [];
				$.each(data, function(event, element) {
					var label = element.name;
					if (label === undefined) {
						label = element.title;
					}
					console.log(element.url + ":" + label);
					searchResults.push(label);
					resultMap[label] = element;
				});
	          	return searchResults;
			}
		}
	});

	$('#search-query').on('typeahead:selected', function(event, element) {
		var key = element.value;
		var url = resultMap[key].url;
		document.location = context + "/" + url; 
	});
}
