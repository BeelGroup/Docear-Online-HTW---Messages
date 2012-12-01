require ['views/NodeView'],  (NodeView) ->  

  view = NodeView.create()

  $("#mvc").append(view.render().el)


