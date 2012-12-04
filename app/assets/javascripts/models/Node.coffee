define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->
  module.create = (properties) ->
    return new Node(properties)

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, graph, childs) ->
      super id, folded, nodeText, graph
      @set 'childs', childs

    defaults:
      points: 0

    render: () ->
      console.log "reder node {@id}"

  module.exports = Node