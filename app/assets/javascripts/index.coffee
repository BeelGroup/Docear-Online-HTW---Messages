$ ->
  initializeJsPlumb()

  $mindmap = $("#mindmap")

  if $mindmap.length > 0

    mindMap = new MindMap("foo2")
    mindMap.appendLeft(new Node("laaaaaaaaaaaaaaaaaa"))
    mindMap.appendRight(new Node("awfnawfn"))
    mindMap.appendRight(new Node("test3"))
    mindMap.appendRight(new Node("“I’m not a big fan of this area of research […]. I know it's in the call for papers, but I think that’s a mistake.”"))
    mindMap.appendRight(new Node("<html><head></head><body><h1>ada</h1></body></html>"))

    simple = new MindMap("root")
    right1 = new Node("right 1")
    right1_3 = new Node("right 1.3")
    right1_3.children = [new Node("right 1.3.1"), new Node("right 1.3.2")]
    right1.children = [new Node("right 1.1"), new Node("right 1.2"), right1_3]
    simple.appendRight(right1)
    simple.appendRight(new Node("right 2<br><b>with HTML</b>"))
    left1= new Node("left 1")
    left1.children = [new Node("left 1.1"), new Node("left 1.2")]
    left2= new Node("left 2")
    left2.children = [new Node("left 2.1"), new Node("left 2.2"), new Node("left 2.3")]
    simple.appendLeft(left1)
    simple.appendLeft(left2)
    #TODO dom ignores height attribute of image
    simple.appendRight(new Node("right 3<br><img src='/assets/images/docear/logo/main-logo.png' style='height: 68px; width: 350px' />"))
    drawer = new MindMapDrawer(simple, $mindmap)
    drawer.draw()
  
  getRecursiveChildren = (node, childrenData)->
    children = []
    if childrenData != undefined
      for child in childrenData
        if child.nodeText != ""
          newChild = new Node(child.nodeText)
          if child.children != undefined
            newChild.children = getRecursiveChildren(newChild, child.children)
          else 
            newChild.children = []
          children.push newChild
    children
  
  $(".loadMap").click -> 
    href = $(this).attr("href")
    recall = (data)->
      $("#mindmap").html("")
      mm = new MindMap(data.root.nodeText)
      
      if data.root.leftChildren != undefined && data.root.leftChildren.nodeText != ""
        leftNode = new Node(data.root.leftChildren.nodeText)
        leftNode.children = getRecursiveChildren(leftNode, data.root.leftChildren)
        mm.appendLeft(leftNode)
      
      if data.root.rightChildren != undefined && data.root.rightChildren.nodeText != ""
        rightNode = new Node(data.root.rightChildren.nodeText)
        if data.root.rightChildren.nodeText == undefined
          console.log "UNDEFINED" 
        rightNode.children = getRecursiveChildren(rightNode, data.root.rightChildren)
        mm.appendRight(rightNode)
      
      drawer = new MindMapDrawer(mm, $("#mindmap"))
      drawer.draw()
      
    $.get(href, recall, "json")
    false
