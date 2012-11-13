$ ->
  #####################
  ### SETUP jsplumb ###
  #####################
  STROKE_COLOR = "#ff0000" #TODO style information should only be in style.less, idea $("#not-visible-example-element").css('color')

  jsPlumb.Defaults.PaintStyle =
    lineWidth: 3,
    strokeStyle: STROKE_COLOR
  jsPlumb.Defaults.Endpoint = ["Dot", { radius:1 }]
  jsPlumb.Defaults.EndpointStyle = { fillStyle:STROKE_COLOR }
  jsPlumb.Defaults.Anchor = ["RightMiddle","LeftMiddle"]
  jsPlumb.Defaults.PaintStyle = { lineWidth: 1, strokeStyle:STROKE_COLOR }
  jsPlumb.Defaults.Connector = [ "StateMachine", { curviness:20 } ]


  #####################
  ### main code     ###
  #####################
  $.getJSON "/rest/v1/user/xyz/mindmap/1.json", (data) ->
    console.log data

  jsPlumb.connect({source:"element1", target:"element2"});
  jsPlumb.connect({source:"element1", target:"element3"});