function withLeadingZero(number) {
	if(number.length == 1) return "0" + number;
	return number;
}
function completeYear(year) {
	if(year.length == 2) {
		var currentYear = new Date().getFullYear();
		if (parseInt(year) <= currentYear % 100)
			return Math.floor(currentYear/100) + year;
		else
			return (Math.floor(currentYear/100) - 1) + year;
	}
	return year
}
function parseDate(rawDate) {
	var separator = "[ .,\\-/] ?";
	var day = "(0?[1-9]|[12][0-9]|3[01])";
	var processedDate = rawDate.replace(/Jan(?:uary)?/i, "01")
		.replace(/Feb(?:ruary)?/i, "02")
		.replace(/Mar(?:ch)?/i, "03")
		.replace(/Apr(?:il)?/i, "04")
		.replace(/May/i, "05")
		.replace(/Jun(?:e)?/i, "06")
		.replace(/Jul(?:y)?/i, "07")
		.replace(/Aug(?:ust)?/i, "08")
		.replace(/Sep(?:tember)?/i, "09")
		.replace(/Oct(?:ober)?/i, "10")
		.replace(/Nov(?:ember)?/i, "11")
		.replace(/Dec(?:ember)?/i, "12")
	var month = "(Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|" +
		"Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?|0?[1-9]|1[0-2])";
	var year = "(\\d{4}|\\d{2})";
	var dmy = new RegExp("^" + day + separator + month + separator + year + "$","i");
	var mdy = new RegExp("^" + month + separator + day + separator + year + "$","i");
	var ymd = new RegExp("^" + year + separator + month + separator + day + "$","i");
	var my = new RegExp("^" + month + separator + year + "$","i");
	var y = new RegExp("^" + year + "$");
	//dmy.exec(rawDate);
	//return RegExp.$1 + "-" + RegExp.$2 + "-" + RegExp.$3;
	if (dmy.test(rawDate)) {
		console.log("dmy");
		return processedDate.replace(dmy, function (match, p1, p2, p3) { return withLeadingZero(p1) + "-" + withLeadingZero(p2) + "-" + completeYear(p3); });
	} else if (mdy.test(rawDate)) {
		console.log("mdy");
		return processedDate.replace(mdy, function (match, p1, p2, p3) { return withLeadingZero(p2) + "-" + withLeadingZero(p1) + "-" + completeYear(p3); });
	} else if (ymd.test(rawDate)) {
		console.log("ymd");
		return processedDate.replace(ymd, function (match, p1, p2, p3) { return withLeadingZero(p3) + "-" + withLeadingZero(p2) + "-" + completeYear(p1); });
	} else if (my.test(rawDate)) {
		console.log("my");
		return processedDate.replace(my, function (match, p1, p2) { return "01-" + withLeadingZero(p1) + "-" + completeYear(p2); });
	} else if (y.test(rawDate)) {
		console.log("y");
		return processedDate.replace(y, function (match, p1) { return "01-01-" + completeYear(p1); });
	}
}