define ['views/NodeView'], (NodeView) ->
  module = ->
  
  class RootNodeView extends NodeView

    template: Handlebars.templates['RootNode']

    constructor: (model) ->
      super model


    renderChilds: () -> 
      @verticalSpacer = 10
      
      childrenRight = @model.get 'rightChildren'
      childrenLeft = @model.get 'leftChildren'
      @adjustNodeHierarchy(@model, childrenRight, 'rightTree')
      @adjustNodeHierarchy(@model, childrenLeft, 'leftTree')
      @centerInContainer()
      jsPlumb.repaintEverything()
      @refreshDom()
      # !!! Todo: add collaps/expand functionality
      #@collapsFoldedNodes()
      # !!!

    collapsFoldedNodes:()->      
      foldedNodes = $('.node.folded')
      $(foldedNodes).children('.children').hide()
      $(foldedNodes).find("i.fold").toggleClass('icon-minus-sign')
      $(foldedNodes).find("i.fold").toggleClass('icon-plus-sign')

    #
    # Refresh the mind map an reposition the dom elements
    #
    refreshDom: () ->
      height = @alignChildrenofElementWithID(@model.get 'id')
      jsPlumb.repaintEverything()
      height

    centerInContainer: ->
      container = $('#'+@model.get 'containerID')
      node = $('#'+@model.get 'id')

      halfContainerWidth = $(container).width() / 2
      halfContainerHeight = $(container).height() / 2
      halfNodeWidth = $(node).width()*($(node).css 'zoom') / 2
      halfNodeHeight = $(node).height()*($(node).css 'zoom') / 2

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