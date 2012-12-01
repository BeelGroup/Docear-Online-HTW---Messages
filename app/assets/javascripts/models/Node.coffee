define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->
  module.create = (properties) ->
    return new Node(properties)


  class Node extends AbstractNode
    constructor: (id, folded, nodeText, @children) ->
      super id, folded, nodeText

    render: () ->
      console.log "reder node {@id}"