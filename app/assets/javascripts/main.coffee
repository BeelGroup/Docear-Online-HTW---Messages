$ ->
  initializeJsPlumb()

  $.getJSON "/rest/v1/user/xyz/mindmap/1.json", (data) ->
    console.log data

  jsPlumb.connect({source:"element1", target:"element2"});
  jsPlumb.connect({source:"element1", target:"element3"});

  mindMap = new MindMap("foo2")
  mindMap.appendLeft(new Node("laaaaaaaaaaaaaaaaaa"))
  mindMap.appendRight(new Node("awfnawfn"))
  mindMap.appendRight(new Node("test3"))
  mindMap.appendRight(new Node("“I’m not a big fan of this area of research […]. I know it's in the call for papers, but I think that’s a mistake.”"))
  mindMap.appendRight(new Node("<html><head></head><body><h1>ada</h1></body></html>"))