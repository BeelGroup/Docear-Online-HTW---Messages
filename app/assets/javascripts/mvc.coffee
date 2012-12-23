require ['views/NodeView', 'models/Node'],  (NodeView, NodeModel) ->  

  model = new NodeModel('4711','0','My awesome node!','Da graph','1')
  view = new NodeView(model)



  $("#mvc").append(view.render().el)

