require ['views/NodeView', 'views/HtmlView', 'models/Node'],  (NodeView,HtmlView,NodeModel) ->  
  
  model = new NodeModel('4711','0','My awesome node!','Da graph','1', 0, 0)
  view = new NodeView(model)
 
  model = new NodeModel('23','12','My awesome second node!','The graph','0', 350, 150)
  view1 = new NodeView(model) 

  # dummy html 
  theHtml = '<table border="1">
<tr>
<td>row 1, cell 1</td>
<td>row 1, cell 2</td>
</tr>
<tr>
<td>row 2, cell 1</td>
<td>row 2, cell 2</td>
</tr>
</table>'

  # create subview  
  myHtml = new HtmlView(new Backbone.Model pureHtml: theHtml)
  # add it to the node
  view.subView(myHtml)

  
  $('#mindmap').append(view.render().el)
  $('#mindmap').append(view1.render().el)