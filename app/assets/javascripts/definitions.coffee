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
  ###*
   Constructs a MindMap.
   @param {String} content of the root node (text or HTML)
  ###
  constructor: (@content = "") ->
    @leftChildren = []
    @rightChildren = []

  getContent: () -> @content

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
  ###*
   Constructs a node.
   @param {String} content of the node (text or HTML)
   @param {Node[]} children childnotes
  ###
  constructor: (@content = "", @children = []) ->

  getContent: () -> @content

class MindMapDrawer
  constructor: (@mindMap, @$target) ->
    @x = ""


  _drawBox = (content, attributes = {}) ->
    attributesToString =
    "<div #{asXmlAttributes(attributes)} class='node'>#{content}</div>"

  ###*
    Draws the mind map into a jQuery selected field
    @param {object} jQUery selected field where to draw the mind map
  ###
  draw: ($target = @$target) ->
    console.log "drawing"
    rootNodeId = "root" #TODO find better system for ids
    $target.append(_drawBox(@mindMap.getContent(), {id:rootNodeId}))
    for i in [0..@mindMap.rightChildren.length]
      child = @mindMap.rightChildren[i]
      childCssId = "child-#{i}"
      $target.append(_drawBox(child.getContent(), {id:childCssId, style: "left: 50px; top: #{i * 45}px; position: relative"}))
      jsPlumb.connect({source:rootNodeId, target:childCssId});


###*
  Converts a flat object to a xml style list of attributes.
  Example:
    input: {"href":"/index.html", "title" : "the title"}
    output: "href='/index.html' title='the title""
###
asXmlAttributes = (attributeDocument) ->
  result = for key, value of attributeDocument
    "#{key}='#{value}'"
  result.reduceRight (x, y) -> x + " " + y