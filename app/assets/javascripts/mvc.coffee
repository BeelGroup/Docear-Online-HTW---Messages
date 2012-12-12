require ['views/NodeView', 'models/Node'],  (NodeView, NodeModel) ->  

  model = new NodeModel('0','unfolded','My awesome node!','Da graph','1')
  view = new NodeView(model)

  
  element = document.createElement("BUTTON");

  model.set 'id', 'bar'
  model.set 'purehtml', element


  $("#mvc").append(view.render(model).el)

