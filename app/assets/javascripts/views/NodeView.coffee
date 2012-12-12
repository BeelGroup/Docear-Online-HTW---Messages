#define ['models/Node', 'text!views/templates/Node.handlebars'], (nodeModel, nodeTemplate) ->
define ['models/Node'], (nodeModel) ->
  module = ->
  ## representing one node
  ## example: 
  ## view = new NodeView

  class NodeView extends Backbone.View

    tagName: 'div',
    className: 'node'	

      ## template will be moved to /templates
    temporarayTemplate = '<div class="entry">
    <h1>id: {{id}}</h1>
    <div>{{{mydiv}}}</div>
    <div class="body">folded: {{folded}} nodeText: {{nodeText}} graph: {{graph}}
    </div>
    </div>'

      ## just for debugging
    innerStats = {title: "My New Post", body: "This is my first post!"}
    
    template: Handlebars.compile temporarayTemplate

    # a.k.a. constructor
    initialize: (@model) ->

    # define events -> here u can pass informations to the model
    events: ->
    ##'click .element': alert 'click'

    subView: (view, autoRender = false) ->
      # if model is set, use izs id OR a unique random id
      viewId = view.model?.id or String(Math.random() * new Date().getTime())
      # add view to subviews
      _subViews[viewId] = view
      # render, when it was added
      if autoRender is true
        view.render()
    
      view

    getRenderData: ->
    # if the model is already set, parse it to json
      if @model?
        @model.toJSON()
    # otherwise pass no informations to fill 
      else
        {}


    # extend the view with the jsPlumb div (which was added to the model) here
    afterRender: ->
      @$el.append(@model.get 'purehtml')
   
    render: ->
      @$el.html @template @getRenderData()
      # render the subviews
      for viewId, view of @_subViews
        view.render()
      # extend the ready rendered htlm element
      @afterRender()
      @

    destroy: ->
      @model?.off null, null, @

      # destroy all subviews
      for viewId, view of @_subViews
        view.destroy()

      @$el.remove()

    leave: (done = ->) ->
      @destroy()
      done()


  # export da class instead of a da module, mtfckr
  module.exports = NodeView