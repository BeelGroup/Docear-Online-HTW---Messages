require ['views/NodeView', 'views/HtmlView', 'models/Node'],  (NodeView,HtmlView,NodeModel) ->  

  # id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, childs
  nodeModel01 = new NodeModel('ID_396380598',false,  '... and hence enable you to benefit from Docear´s full functionality', false, 350, 150, 0, 0, null)
  nodeModel02 = new NodeModel('ID_1443707089', false, '<html>\n  <head>\n    \n  </head>\n  <body>\n    <p>\n      That will make your work much more effective<br>and save you lots of time\n    </p>\n  </body>\n</html>', true, 0, 0, 0, 0, null)

  view = new NodeView(nodeModel01)
  view1 = new NodeView(nodeModel02) 

  $('#mindmap').append(view.render().el)
  $('#mindmap').append(view1.render().el)