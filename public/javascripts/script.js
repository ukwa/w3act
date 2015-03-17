$(function () {
	$("#start-date").datepicker({ dateFormat: "dd-mm-yy", changeYear: true });
	$("#end-date").datepicker({ dateFormat: "dd-mm-yy", changeYear: true });
	$("#refusal-date").datepicker({ dateFormat: "dd-mm-yy", changeYear: true });
	$("#log-date").datepicker({ dateFormat: "dd-mm-yy", changeYear: true });
	$("#date_of_publication").datepicker({ dateFormat: "dd-mm-yy", changeYear: true });
	$("#publicationDate").datepicker({ dateFormat: "dd-mm-yy", changeYear: true });
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
