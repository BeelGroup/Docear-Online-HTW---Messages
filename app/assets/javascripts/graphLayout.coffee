cssForClass = (className, property) ->
  className = className.replace(".","")
  if $("." + className).length < 1
    $("<div id='remove-tmp-element' class='#{className}'>").attr('type','hidden').appendTo('body');
  result = $("." + className).css(property)
  $("#remove-tmp-element").remove()
  result

initializeJsPlumb = ->
  STROKE_COLOR = "#ff0000" #TODO style information should only be in style.less, idea $("#not-visible-example-element").css('color')

  jsPlumb.Defaults.PaintStyle =
    lineWidth: 3,
    strokeStyle: STROKE_COLOR
  jsPlumb.Defaults.Endpoint = ["Dot", { radius:1 }]
  jsPlumb.Defaults.EndpointStyle = { fillStyle:STROKE_COLOR }
  jsPlumb.Defaults.Anchor = ["RightMiddle","LeftMiddle"]
  jsPlumb.Defaults.PaintStyle = { lineWidth: 2, strokeStyle:STROKE_COLOR }
  jsPlumb.Defaults.Connector = [ "StateMachine", { curviness:10 } ] # Bezier causes drawing errors on Firefox 16.0.2 ubuntu
  $(window).resize ->
    jsPlumb.repaintEverything()

class MindMap
  #
  # Constructs a MindMap.
  # @param [String] content of the root node (text or HTML)
  #
  constructor: (@content = "") ->
    @leftChildren = []
    @rightChildren = []

  getContent: () -> @content

  _append: (children, node) -> children.push node

  #
  # Appends a childnode on the left side.
  # @param [Node] node the node to appand
  #
  appendLeft: (node) -> @_append @leftChildren, node

  #
  # Appends a childnode on the right side.
  # @param [Node] node the node to appand
  #
  appendRight: (node) -> @_append @rightChildren, node

#
#  A node in a MindMap
#
class Node
  #
  # Constructs a node.
  # @param [String] content of the node (text or HTML)
  # @param [Node[]] children childnotes
  #
  constructor: (@content = "", @children = []) ->

  getContent: () -> @content

$.fn.extend
  docear: ->
    $element = this

    dimension: -> width: $element.width(), height: $element.height()

    coordinatesForHorizontalAlignmentInBox: ($box) -> $box.width() / 2 - $element.width()
    coordinatesForVerticalAlignmentInBox: ($box) -> $box.height() / 2 - $element.height()

    centerInBox: ($box) ->
      newX = @coordinatesForHorizontalAlignmentInBox $box
      newY = @coordinatesForVerticalAlignmentInBox $box
      @move left: newX, top: newY

    #
    # moves this element to new coordinates
    # @param [Object] target coordinates like {left: 0, top: 0}
    move: (coordinates = {left: 0, top: 0}) ->
      if coordinates.left then $element.css("left", coordinates.left)
      if coordinates.top then $element.css("top", coordinates.top)

class MindMapDrawer
  constructor: (@mindMap, @$target) ->
    @x = ""

  _drawBox = (content, attributes = {}) ->
    attributesToString =
    "<div #{asXmlAttributes(attributes)} class='node'>#{content}</div>"

  #
  # Draws the mind map into a jQuery selected field
  # @param [object] jQUery selected field where to draw the mind map
  #
  draw: ($target = @$target) ->
    console.log "drawing"
    rootNodeId = "root" #TODO find better system for ids
    $target.append(_drawBox(@mindMap.getContent(), {id:rootNodeId, style: ""}))
    $root = $("#" + rootNodeId)
    $root.docear().centerInBox $root.parent()

#
#  Converts a flat object to a xml style list of attributes.
#  Example:
#    input: {"href":"/index.html", "title" : "the title"}
#    output: "href='/index.html' title='the title""
#
asXmlAttributes = (attributeDocument) ->
  result = for key, value of attributeDocument
    "#{key}='#{value}'"
  result.reduceRight (x, y) -> x + " " + y