define ['views/NodeView'], (NodeView) ->
  module = ->
  
  class RootNodeView extends NodeView

    initialize: (model) ->
      super model


    renderChilds: () -> 
      @verticalSpacer = 10
      
      childrenRight = @model.get 'rightChildren'
      childrenLeft = @model.get 'leftChildren'
      
      @adjustNodeHierarchy(@model, childrenRight, 'rightTree')
      @adjustNodeHierarchy(@model, childrenLeft, 'leftTree')
      
      $root = $('#'+@model.get 'id')
      $root.addClass('root')
      @centerNodeInContainer($('#mindmap'), $root)
      
      @refreshDom()

    render: ->
      @$el.html @template @getRenderData()
      @recursiveRender @, @model.get 'rightChildren'
      @recursiveRender @, @model.get 'leftChildren'
      # render the subviews
      for viewId, view of @subViews
        html = view.render().el
        $(html).appendTo(@el)
      # extend the ready rendered htlm element
      @afterRender()
      @positioning()
      @    



  module.exports = RootNodeView