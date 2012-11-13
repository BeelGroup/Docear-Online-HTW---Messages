initializeJsPlumb = ->
  STROKE_COLOR = "#ff0000" #TODO style information should only be in style.less, idea $("#not-visible-example-element").css('color')

  jsPlumb.Defaults.PaintStyle =
    lineWidth: 3,
    strokeStyle: STROKE_COLOR
  jsPlumb.Defaults.Endpoint = ["Dot", { radius:1 }]
  jsPlumb.Defaults.EndpointStyle = { fillStyle:STROKE_COLOR }
  jsPlumb.Defaults.Anchor = ["RightMiddle","LeftMiddle"]
  jsPlumb.Defaults.PaintStyle = { lineWidth: 1, strokeStyle:STROKE_COLOR }
  jsPlumb.Defaults.Connector = [ "StateMachine", { curviness:20 } ]

class MindMap
  constructor: () ->
    @leftChildren = []
    @rightChildren = []
    @root = new Node

  _append: (children, node) -> children.push node
  ###*
   Appends a childnode on the left side.
   @param {Node} node the node to appand
  ###
  appendLeft: (node) -> @_append @leftChildren, node
  ###*
   Appends a childnode on the right side.
   @param {Node} node the node to appand
  ###
  appendRight: (node) -> @_append @rightChildren, node

###*
  A node in a MindMap
###
class Node
  constructor: (@content = "") ->
