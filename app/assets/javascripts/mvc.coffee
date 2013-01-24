require ['MapStarter'],  (MapStarter) ->  


    initializeJsPlumb()
    ## ##############################################
    start = new MapStarter('PASS JSON HERE')    ##!!!
    ## ##############################################
    start.positionNodes()


    fontSize = 14
    # fontSize = parseInt($(".inner-node:first").css("font-size").replace("px", ""))
    zoom = 1
    setZoom: (zoomFactor) ->
      zoom = zoomFactor
      $('#mindmap .node').css("font-size", fontSize*zoom+"px")
      $('#mindmap .node').css("line-height", fontSize*zoom+"px")
      @refreshDom
