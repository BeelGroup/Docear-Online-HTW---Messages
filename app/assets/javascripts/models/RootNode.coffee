define ['models/AbstractNode'],  (AbstractNode) ->  
  module = ->
  
  class RootNode extends AbstractNode
    constructor: (id, folded, nodeText, containerID, isHTML, xPos, yPos, hGap, shiftY, locked) ->
      super id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked
      @set 'containerID', containerID
      @set 'leftChildren', []
      @set 'rightChildren', []

    # overwriting getter @get 'children' since we RootNode does not have a children attr
    get: (attr)->
      if attr == 'children'
        return @getChildren()
      Backbone.Model.prototype.get.call(this, attr);
  
      
    getChildren: ->
      children = []
      children = $.merge(children, @get('leftChildren').slice()  )
      children = $.merge(children, @get('rightChildren').slice()  )
      children
      
    getNextLeftChild: ->
      @getNextChild @get 'leftChildren'
    
    getNextRightChild: ->
      @getNextChild @get 'rightChildren'
      
  module.exports = RootNode