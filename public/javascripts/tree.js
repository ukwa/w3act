function fillTree (fieldName, jsonData) {
	$("#" + fieldName + "Tree").dynatree({
		checkbox: true,
		selectMode: 2,
		children: jsonData,
		onSelect: function(select, node) {
			// Get a list of all selected nodes, and convert to a key array:
			var selKeys = $.map(node.tree.getSelectedNodes(), function(node){
				return node.data.key;
			});
			$("#" + fieldName)[0].value = selKeys.join(", ");
			// Get a list of all selected TOP nodes
			var selRootNodes = node.tree.getSelectedNodes(true);
			// ... and convert to a key array:
			var selRootKeys = $.map(selRootNodes, function(node){
				return node.data.key;
			});
		}
	});
	var elementsToSelect = $("#" + fieldName)[0].value.split(", ");
	for (i = 0; i < elementsToSelect.length; i++) {
		var element = elementsToSelect[i];
		$("#" + fieldName + "Tree").dynatree("getTree").selectKey(element);
	}
}