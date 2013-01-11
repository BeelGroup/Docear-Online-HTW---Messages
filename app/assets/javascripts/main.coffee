$ ->
  if $("body").hasClass("login-page")
    $("#username").focus()

  $.ajax({
    type: 'GET',
    url: '/maps',
    dataType: 'json',
    success: (data)->
      $selectMinmap = $('#select-mindmap')
      $.each(data, (index,value)->
        $selectMinmap.append '<li><a class="loadMap dropdown-toggle" href="/map/json/'+value.mmIdOnServer+'"> '+value.fileName+'</a></li>'
      )
      '/map/json/id'
  })