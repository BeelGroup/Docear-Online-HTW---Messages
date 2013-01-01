define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, childs, locked) ->
      super id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked
      # silent to avoid throwing an event
      @set 'childs', childs, silent: true



  module.exports = Node