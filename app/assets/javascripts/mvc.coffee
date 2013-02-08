require ['views/MapView', 'routers/DocearRouter'],  (MapView, DocearRouter) ->  

  
  initializeJsPlumb()
  router = new  DocearRouter()
  
  ## ##############################################
  mapView = new MapView('PASS JSON HERE')    ##!!!
  ## ##############################################
  
  ## just for debugging
  mapView.render()


  ###
  fontSize = 14
  # fontSize = parseInt($(".inner-node:first").css("font-size").replace("px", ""))
  zoom = 1
  setZoom: (zoomFactor) ->
    zoom = zoomFactor
    $('#mindmap .node').css("font-size", fontSize*zoom+"px")
    $('#mindmap .node').css("line-height", fontSize*zoom+"px")
    @refreshDom
  ###