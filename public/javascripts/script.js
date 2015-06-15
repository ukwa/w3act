$(function () {
	$("#start-date").datepicker({ dateFormat: "dd-mm-yy" });
	$("#end-date").datepicker({ dateFormat: "dd-mm-yy" });
	$("#refusal-date").datepicker({ dateFormat: "dd-mm-yy" });
	$("#date_of_publication").datepicker({ dateFormat: "dd-mm-yy" });
	$('a[rel="external"]').attr('target', '_blank');
});

function getURLParameter(param) {
	var pageUrl = window.location.search.substring(1);
	var urlVariables = pageUrl.split('&');
	for (var i=0; i<urlVariables.length; i++) {
		var parameterName = urlVariables[i].split('=');
		if (parameterName[0] == param) {
			return parameterName[1];
		}
	}				
}

function modalLoader() {
	$('#modalLoader').modal({
	    backdrop: true,
	    keyboard: true
	});
}

function wayBackLoader() {
	$('#importWayback').on('click', function(event) {
		modalLoader();
	});
}