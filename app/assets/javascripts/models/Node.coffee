define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, graph, childs, xOffset, yOffset) ->
      super id, folded, nodeText, graph, xOffset, yOffset
      # silent to avoid throwing an event
      @set 'childs', childs, silent: true



  module.exports = Node