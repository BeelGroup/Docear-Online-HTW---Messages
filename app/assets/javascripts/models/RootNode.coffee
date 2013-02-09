define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->
  
  class RootNode extends AbstractNode
    constructor: (id, folded, nodeText, containerID, isHTML, xPos, yPos, hGap, shiftY, locked) ->
      super id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked
      @set 'containerID', containerID
      @set 'leftChildren', []
      @set 'rightChildren', []
      
  module.exports = RootNode