require ['views/NodeView', 'models/Node'],  (NodeView, NodeModel) ->  

  view = new NodeView()
  model = new NodeModel('0','unfolded','My awesome node!','Da graph','1')

  model.set 'id', 'bar'

  console.log model.toJSON()

  $("#mvc").append(view.render(model).el)

