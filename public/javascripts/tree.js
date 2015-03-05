function fillTree (fieldName, jsonData) {
	var treeElem = $("#" + fieldName + "Tree");
	treeElem.dynatree({
		checkbox: true,
		selectMode: 2,
		children: jsonData,
		onSelect: function(select, node) {
			// Get a list of all selected nodes, and convert to a key array:
			var selKeys = $.map(node.tree.getSelectedNodes(), function(node){
				return node.data.key;
			});
			$("#" + fieldName)[0].value = selKeys.join(", ");
			$("#" + fieldName)[0].onchange();
		},
		onPostInit: function(isReloading, isError) {
			var tree = treeElem.dynatree('getTree');
			$.map(tree.getSelectedNodes(), function(node){
				node.makeVisible();
			});
		}
	});
}