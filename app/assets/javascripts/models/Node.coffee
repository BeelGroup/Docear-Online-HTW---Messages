define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked) ->
      @set children = []
      super id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked
      



  module.exports = Node