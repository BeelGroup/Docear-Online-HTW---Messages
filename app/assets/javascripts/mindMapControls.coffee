
  
  changeZoom = (direction)->
    ## global variable?? :P
    ###
    zoomFactor = 0;
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
    ###
    #current_map_drawer.refreshDom() 
    
  $('#mindmap').mousewheel (e, delta)-> 
    changeZoom(e.originalEvent.detail)
    false
  
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