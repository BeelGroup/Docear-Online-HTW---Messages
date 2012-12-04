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
