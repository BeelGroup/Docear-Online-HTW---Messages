require ['MapStarter'],  (MapStarter) ->  


    initializeJsPlumb()
    ## ##############################################
    start = new MapStarter('PASS JSON HERE')    ##!!!
    ## ##############################################
    start.positionNodes()


    $('body').on('click', ".loadMap", 
      ->
      href = $(this).attr("href")
      $(this).closest(".dropdown").children(".dropdown-toggle").click()
      recall = (data)->
        $("#mindmap").html("")
        jsPlumb.reset()
        
        rootNode = new RootNodeModel(data.root.id, data.root.folded == "true", data.root.nodeText)
        
        if data.root.leftChildren != undefined
          leftNodes = getRecursiveChildren(data.root.leftChildren)
          rootNode.set 'leftChildren', leftNodes
        
        if data.root.rightChildren != undefined
          rightNodes = getRecursiveChildren(data.root.rightChildren)
          rootNode.set 'rightChildren', rightNodes
        
        generateViews rootNode
        mindMapPositioner = new Positioner($('#mindmap'), rootNode)
        
        foldedNodes = $('.node.folded')
        $(foldedNodes).children('.children').hide()
        $(foldedNodes).find("i.fold").toggleClass('icon-minus-sign')
        $(foldedNodes).find("i.fold").toggleClass('icon-plus-sign')
        
      $.get(href, recall, "json")
      false
    )