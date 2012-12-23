define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, graph, childs) ->
      super id, folded, nodeText, graph
      # silent to avoid throwing an event
      @set 'childs', childs, silent: true



  module.exports = Node