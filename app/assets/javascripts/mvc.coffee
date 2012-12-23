require ['views/NodeView', 'views/HtmlView', 'models/Node'],  (NodeView,HtmlView,NodeModel) ->  
  
  model = new NodeModel('4711','0','My awesome node!','Da graph','1')
  view = new NodeView(model)
  
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

  
  $("#mvc").append(view.render().el)

