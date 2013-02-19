define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->

  class Node extends AbstractNode
    constructor: (id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked, parent) ->
      super id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked	
      @set 'children', []
      @set 'parent', parent
    
  module.exports = Node