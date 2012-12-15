$ ->
  initializeJsPlumb()

  $mindmap = $("#mindmap")
  current_mindmap = null
  fontSize = 14
  zoom = 1
  zoomFactor = 0

  if $mindmap.length > 0

    mindMap = new MindMap("foo2")
    mindMap.appendLeft(new Node("laaaaaaaaaaaaaaaaaa"))
    mindMap.appendRight(new Node("awfnawfn"))
    mindMap.appendRight(new Node("test3"))
    mindMap.appendRight(new Node("“I’m not a big fan of this area of research […]. I know it's in the call for papers, but I think that’s a mistake.”"))
    mindMap.appendRight(new Node("<html><head></head><body><h1>ada</h1></body></html>"))

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
  
  $(".loadMap").click -> 
    href = $(this).attr("href")
    recall = (data)->
      $("#mindmap").html("")
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
      
    $.get(href, recall, "json")
    false
  
  $( '#mindmap' ).draggable({
    cancel: "a.ui-icon",
    containment: "mindmap-container",
    cursor: "move"
  });
  
  changeZoom = (direction)->
    if direction > 0
      zoom = zoom / 1.5
      zoomFactor--
    else
      zoom = zoom * 1.5
      zoomFactor++
    
    $("#zoom-factor").text(zoomFactor);
    
    $("#mindmap").html("")
    current_map_drawer = new MindMapDrawer(current_mindmap, $("#mindmap"))
    current_map_drawer.setZoom(zoom)
    current_map_drawer.draw()	
    
  $('#mindmap').mousewheel (e, delta)-> 
    changeZoom(e.originalEvent.detail)
    false
  
  $('#zoom-in').click ->
  	changeZoom(-1)
  	
  $('#zoom-out').click ->
  	changeZoom(1)