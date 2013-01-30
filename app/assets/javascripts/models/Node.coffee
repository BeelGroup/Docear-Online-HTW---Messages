define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, children, locked) ->
      super id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked
      # silent to avoid throwing an event
      @set 'children', children, silent: true



  module.exports = Node