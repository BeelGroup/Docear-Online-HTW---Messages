$ ->
  initializeJsPlumb()

  $mindmap = $("#mindmap")

  if $mindmap.length > 0
    $.getJSON "/rest/v1/user/xyz/mindmap/1.json", (data) ->
      console.log data

    mindMap = new MindMap("foo2")
    mindMap.appendLeft(new Node("laaaaaaaaaaaaaaaaaa"))
    mindMap.appendRight(new Node("awfnawfn"))
    mindMap.appendRight(new Node("test3"))
    mindMap.appendRight(new Node("“I’m not a big fan of this area of research […]. I know it's in the call for papers, but I think that’s a mistake.”"))
    mindMap.appendRight(new Node("<html><head></head><body><h1>ada</h1></body></html>"))

    simple = new MindMap("root")
    simple.appendRight(new Node("right 1"))
    simple.appendRight(new Node("right 2"))
    simple.appendRight(new Node("right 3"))
    drawer = new MindMapDrawer(simple, $mindmap)
    drawer.draw()

    $(".node").click ->
      $(this).css("background", "red")
      console.log $(this).attr('id')

    $("#root").addClass("selected")
    $("#root").attr("data-next", "child-0")

    #this is hardcoded for a demo
    Mousetrap.bind ['right', 'down'], ->
      $current = $(".selected")
      nextId = $current.attr("data-next")
      nextId = if nextId == "child-3" then "root" else nextId
      $next = $("##{nextId}")
      console.log $next
      $current.removeClass("selected")
      $next.addClass("selected")
