$ ->
  
    
  
  $.ajax({
    type: 'GET',
    url: '/maps',
    dataType: 'json',
    success: (data)->
      $selectMinmap = $('#selct-mindmap')
      $.each(data, (index,value)->
        $selectMinmap.append '<li><a class="loadMap dropdown-toggle" href="/map/json/'+value.mmIdOnServer+'"> '+value.fileName+'</a></li>'
      )
      '/map/json/id'
  })
  
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
  
  $( '#mindmap' ).draggable({
    cancel: "a.ui-icon, .node",
    containment: "mindmap-container",
    cursor: "move",
    handle: "#mindmap"
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
    
    #current_map_drawer.refreshDom() 
    
  $('#mindmap').mousewheel (e, delta)-> 
    changeZoom(e.originalEvent.detail)
    false
  
  $('#zoom-in').click ->
  	changeZoom(-1)
  	
  $('#zoom-out').click ->
  	changeZoom(1)
  
  $('body').on("mouseenter", ".node", 
    -> 
      $(this).children('i.fold:first, .controls').show()
      false
  )
  $('body').on("mouseleave", ".node",
    -> 
      if not $(this).hasClass('selected')
        $(this).children('i.fold:first, .controls').hide()
      false
  )
    
  $('body').on("click", ".node i.fold", 
  	->
  	  if $(this).hasClass('icon-minus-sign')
        $(this).parent().children('.children').fadeOut('fast')
      else
        $(this).parent().children('.children').fadeIn('fast')
      $(this).toggleClass('icon-minus-sign')
      $(this).toggleClass('icon-plus-sign')
    )

  $('body').on("click", ".node .action-edit",
    -> 
      innerNode = $(this).closest('.node').children('.inner-node')  
      if $(this).hasClass('action-save')
        editArea = innerNode.find('textarea:first')
        innerNode.html(editArea.text())
      else
        editArea = $('<textarea class="changeable"></textarea>')
        editArea.css('width', (innerNode.width()+10)+'px')
        editArea.css('height', (innerNode.height()+10)+'px')
        editArea.text(innerNode.html())
        innerNode.empty().append(editArea)
      $(this).toggleClass('icon-ok')
      $(this).toggleClass('icon-edit')
      $(this).toggleClass('action-save')
      false
  )
  
  selectNextChild = (selectedNode, childClass = '')->
    childNodes = $(selectedNode).children('.children:first').children('.node'+childClass+':first')
    if $(childNodes).size() > 0
      $(selectedNode).removeClass('selected')	
      $(childNodes).addClass('selected')
      
  selectParent = (selectedNode)->
    $(selectedNode).removeClass('selected')
    parent = $(selectedNode).parent().closest('.node')
    $(parent).addClass('selected')
  
  selectBrother = (selectedNode, next = true)->
    $(selectedNode).removeClass('selected')
    if next
      $(selectedNode).next('.node').addClass('selected')
    else
      $(selectedNode).prev('.node').addClass('selected')   
    
  
  $("body").keypress (event)->
    selectedNode = $('.node.selected')	
    if $(selectedNode).size() > 0
      switch event.keyCode
        when 37 #LEFT
          if $(selectedNode).hasClass('root-node')
            selectNextChild selectedNode, '.leftTree'
          else if $(selectedNode).hasClass('leftTree')
            selectNextChild selectedNode
          else
            selectParent selectedNode
        when 38 #TOP
          selectBrother selectedNode, false
        when 39 #RIGHT
          if $(selectedNode).hasClass('root-node')
            selectNextChild selectedNode, '.rightTree'	
          else if $(selectedNode).hasClass('rightTree')
            selectNextChild selectedNode
          else
            selectParent selectedNode
        when 40 #DOWN
          selectBrother selectedNode, true
    else if event.keyCode in [37,38,39,40]
      $('#root').addClass('selected')
  