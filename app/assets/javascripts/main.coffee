$ ->
  $.getJSON "/rest/v1/user/xyz/mindmap/1.json", (data) ->
    console.log data