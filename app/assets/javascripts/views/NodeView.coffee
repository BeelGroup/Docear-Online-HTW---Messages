define ['views/AbstractNodeView'], (AbstractNodeView) ->
  module = ->
  
  class NodeView extends AbstractNodeView

    initialize: (model) ->
      super model


    recursiveRender: (parent, nodes)->
      $.each(nodes, (index, node)=>
        nodeView = new NodeView(node)
        $nodeHtml = $($(nodeView.render().el).html())
        $('#mindmap').append($nodeHtml)
        children = node.get 'children'
        if children != undefined
          @recursiveRender(nodeView, children)
      )


    destroy: ->
      @model?.off null, null, @

      # destroy all subviews
      for viewId, view of @subViews
        view.destroy()

      @$el.remove()

    # pass a final function, if u want to
    leave: (done = ->) ->
      @destroy()
      done()


  module.exports = NodeView