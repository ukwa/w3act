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
function formatDate(day, month, year) {
	return withLeadingZero(day) + "-" + withLeadingZero(month) + "-" + completeYear(year);
}
function parseDate(rawDate) {
	var months = [
		"Jan(?:uary)?", "Feb(?:ruary)?", "Mar(?:ch)?",
		"Apr(?:il)?", "May", "Jun(?:e)?",
		"Jul(?:y)?", "Aug(?:ust)?", "Sep(?:tember)?",
		"Oct(?:ober)?", "Nov(?:ember)?", "Dec(?:ember)?"
	];
	var separator = "[ .,\\-/] ?";
	var day = "(0?[1-9]|[12][0-9]|3[01])";
	var month = "(" + months.join("|") + "|0?[1-9]|1[0-2])";
	var year = "(\\d{4}|\\d{2})";
	var dmy = new RegExp("^" + day + separator + month + separator + year + "$","i");
	var mdy = new RegExp("^" + month + separator + day + separator + year + "$","i");
	var ymd = new RegExp("^" + year + separator + month + separator + day + "$","i");
	var my = new RegExp("^" + month + separator + year + "$","i");
	var y = new RegExp("^" + year + "$");
	var processedDate = rawDate;
	for (var i = 0; i < months.length; i++)
		processedDate = processedDate.replace(new RegExp(months[i], "i"), "" + (i + 1));
	if (dmy.test(rawDate))
		return processedDate.replace(dmy,
			function (match, p1, p2, p3) { return formatDate(p1, p2, p3); });
	if (mdy.test(rawDate))
		return processedDate.replace(mdy,
			function (match, p1, p2, p3) { return formatDate(p2, p1, p3); });
	if (ymd.test(rawDate))
		return processedDate.replace(ymd,
			function (match, p1, p2, p3) { return formatDate(p3, p2, p1); });
	if (my.test(rawDate))
		return processedDate.replace(my,
			function (match, p1, p2) { return formatDate("1", p1, p2); });
	if (y.test(rawDate))
		return processedDate.replace(y,
			function (match, p1) { return formatDate("1", "1", p1); });
}