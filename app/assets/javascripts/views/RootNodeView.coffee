define ['views/NodeView'], (NodeView) ->
  module = ->
  
  class RootNodeView extends NodeView

    initialize: (model) ->
      super model

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