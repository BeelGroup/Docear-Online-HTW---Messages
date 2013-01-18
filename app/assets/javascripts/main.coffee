$ ->
  if $("body").hasClass("login-page")
    $("#username").focus()

  $.ajax({
    type: 'GET',
    url: jsRoutes.controllers.ControllerFactory.mindMap.mapListFromDB().url,
    dataType: 'json',
    success: (data)->
      $selectMinmap = $('#select-mindmap')
      $.each(data, (index,value)->
        $selectMinmap.append """<li><a class="loadMap dropdown-toggle" href="#{jsRoutes.controllers.ControllerFactory.mindMap.map(value.mmIdOnServer).url}"> #{value.fileName}</a></li>"""
      )
      '/map/json/id'
  })