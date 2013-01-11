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
    
  
  getRecursiveChildren = (childrenData)->
    children = []
    if childrenData.id != undefined && childrenData.id != null
      newChild = new Node(childrenData.nodeText)
      children.push newChild
    else if childrenData != undefined
      for child in childrenData
        if child.nodeText != ""
          newChild = new Node(child.nodeText)
          newChild.folded = child.folded == "true"
          if child.children != undefined
            newChild.children = getRecursiveChildren(child.children)
          children.push newChild
    children
  
  $("body").on("click", ".loadMap", 
  	-> 
      href = $(this).attr("href")
      $(this).closest(".dropdown").children(".dropdown-toggle").click()
      recall = (data)->
        $("#mindmap").html("")
        jsPlumb.reset()
        current_mindmap = new MindMap(data.root.nodeText)
        
        if data.root.leftChildren != undefined
          leftNode = getRecursiveChildren(data.root.leftChildren)
          for n in leftNode
            current_mindmap.appendLeft(n)
        
        if data.root.rightChildren != undefined
          rightNode = getRecursiveChildren(data.root.rightChildren)
          for n in rightNode
            current_mindmap.appendRight(n)
        
        #if data.root.rightChildren != undefined && data.root.rightChildren.nodeText != ""
        #  rightNode = new Node(data.root.rightChildren.nodeText)
        #  if data.root.rightChildren.nodeText == undefined
        #    rightNode.children = getRecursiveChildren(rightNode, data.root.rightChildren)
        #  current_mindmap.appendRight(rightNode)
        
        current_map_drawer = new MindMapDrawer(current_mindmap, $("#mindmap"))
        current_map_drawer.draw()
        
        foldedNodes = $('.node.folded')
        $(foldedNodes).children('.children').hide()
        $(foldedNodes).find("i.fold").toggleClass('icon-minus-sign')
        $(foldedNodes).find("i.fold").toggleClass('icon-plus-sign')
        
      $.get(href, recall, "json")
      false
  )
  