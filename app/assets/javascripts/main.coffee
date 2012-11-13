$ ->
  initializeJsPlumb()

  $.getJSON "/rest/v1/user/xyz/mindmap/1.json", (data) ->
    console.log data

  jsPlumb.connect({source:"element1", target:"element2"});
  jsPlumb.connect({source:"element1", target:"element3"});