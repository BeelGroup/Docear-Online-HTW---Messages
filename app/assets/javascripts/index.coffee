require ['views/NodeView', 'views/HtmlView', 'models/Node', 'models/RootNode'],  (NodeView,HtmlView,NodeModel,RootNodeModel) ->
  
  initializeJsPlumb()
  
  $mindmap = $("#mindmap")
  current_mindmap = null
  #current_map_drawer = null
  fontSize = 14
  zoom = 1
  zoomFactor = 0

  if $mindmap.length > 0
    
    current_mindmap = new MindMap("root")
    right1 = new Node("right 1")
    right1_3 = new Node("right 1.3")
    right1_3.children = [new Node("right 1.3.1"), new Node("right 1.3.2")]
    right1.children = [new Node("right 1.1"), new Node("right 1.2"), right1_3]
    current_mindmap.appendRight(right1)
    current_mindmap.appendRight(new Node("right 2<br><b>with HTML</b>"))
    left1= new Node("left 1")
    left1.children = [new Node("left 1.1"), new Node("left 1.2")]
    left2= new Node("left 2")
    left2.children = [new Node("left 2.1"), new Node("left 2.2"), new Node("left 2.3")]
    current_mindmap.appendLeft(left1)
    current_mindmap.appendLeft(left2)
    #TODO dom ignores height attribute of image
    current_mindmap.appendRight(new Node("right 3<br><img src='/assets/images/docear/logo/main-logo.png' style='height: 68px; width: 350px' />"))
    current_map_drawer = new MindMapDrawer(current_mindmap, $mindmap)
    current_map_drawer.draw()
    
    #connectionList = jsPlumb.getConnections("child-4");
    #overlay = connectionList.getOverlay("child-4")
    #connectionList.hide()
    #jsPlumb.getInstance()
    #jsPlumb.hide("child-4", false);
    #jsPlumb.selectEndpoints($('#child-4')).hideOverlays
    #jsPlumb.selectEndpoints($('#child-4'))
    #jsPlumb.deleteEndpoint('child-4')
    # connections
    #conns = jsPlumb.getConnections({source:'child-4'})
    #connections = jsPlumb.getConnections(source:$('#child-4'))
    #for conn in connections
    #  jsPlumb.removeAllEndpoints(conn)
    #jsPlumb.removeAllEndpoints("child-4", "child-5");
    #jsPlumb.selectEndpoints({source:$mindmap, source:$("child-4")}).hide
    