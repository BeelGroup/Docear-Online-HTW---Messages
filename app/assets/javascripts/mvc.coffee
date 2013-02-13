require ['MapController', 'routers/DocearRouter'],  (MapController, DocearRouter) ->  

  
  initializeJsPlumb()

  loadUserMaps: ->
    $.ajax({
      type: 'GET',
      url: jsRoutes.controllers.ControllerFactory.mindMap.mapListFromDB().url,
      dataType: 'json',
      success: (data)->
        $selectMinmap = $('#select-mindmap')
        $.each(data, (index,value)->
          $selectMinmap.append """<li><a class="dropdown-toggle" href="#loadMap/#{value.mmIdOnServer}"> #{value.fileName}</a></li>"""
        )
        '/map/json/id'
    })



  ## ##############################################
  mapController = new MapController('PASS JSON HERE')    ##!!!
  ## ##############################################
  router = new  DocearRouter(mapController)
  ## just for debugging
  #mapController.renderDummyMap()


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