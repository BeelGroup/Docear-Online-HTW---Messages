define ['views/NodeView'], (NodeView) ->
  module = ->
  
  class RootNodeView extends NodeView

    template: Handlebars.templates['RootNode']

    initialize: (model) ->
      super model


    renderChilds: () -> 
      @verticalSpacer = 10
      
      childrenRight = @model.get 'rightChildren'
      childrenLeft = @model.get 'leftChildren'
      
      @adjustNodeHierarchy(@model, childrenRight, 'rightTree')
      @adjustNodeHierarchy(@model, childrenLeft, 'leftTree')
      jsPlumb.repaintEverything()
      @centerRootNodeInContainer()
      @refreshDom()

    centerRootNodeInContainer: ->
      container = $('#'+@model.get 'containerID')
      node = $('#'+@model.get 'id')

      halfContainerWidth = $(container).width()/2
      halfContainerHeight = $(container).height()/2
      halfNodeWidth = $(node).width()/2
      halfNodeHeight = $(node).height()/2

      pos = 
        x: halfContainerWidth - halfNodeWidth
        y: halfContainerHeight - halfNodeHeight

      @model.set 'Pos', pos
        


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
      @    



  module.exports = RootNodeView