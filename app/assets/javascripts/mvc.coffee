require ['MapController', 'routers/DocearRouter'],  (MapController, DocearRouter) ->  

  
  initializeJsPlumb()

  mapController = new MapController('PASS JSON HERE')

  router = new  DocearRouter(mapController)
