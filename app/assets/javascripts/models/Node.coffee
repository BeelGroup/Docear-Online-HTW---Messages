require ['models/AbstractNode'],  (AbstractNode) ->  

  console.log AbstractNode

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, @children) ->
      super id, folded, nodeText

    render: () ->
      console.log "reder node {@id}"