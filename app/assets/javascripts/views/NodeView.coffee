define ['models/Node', 'views/SyncedView'], (nodeModel, SyncedView) ->
  module = ->
  
  class NodeView extends SyncedView

    tagName: 'div',
    className: 'node'	
    subViews: {}
    template: Handlebars.templates['Node']

    fieldMap:
      '#nodeId': 'id'
  

    # a.k.a. constructor
    initialize: (@model) ->
      super
      # render, when model was changed
      #@model.on 'change', @render, @

    # define events -> here u can pass informations to the model
    events: =>
      'click .changeable': 'fadeInButton'
      'click .acceptChanges': 'updateModel'
    

    fadeInButton: -> 
      console.log 'fade in "save changes" button'

    updateModel: ->      
      # update changed values to the model
      @model.set 'id', $('.changeable').val() 
      console.log @model
      @model.set 'id', '4799'


    subView: (view, autoRender = false) ->
      # if model is set, use its id OR a unique random id
      viewId = view.model?.id or String(Math.random() * new Date().getTime())
      # add view to subviews
      @subViews[viewId] = view
      view

    getRenderData: ->
    # if the model is already set, parse it to json
      if @model?
        @model.toJSON()
    # otherwise pass an empty JSON
      else
        {}


    # extend the view with the jsPlumb div
    afterRender: ->
      @$el.append(@model.get 'purehtml')


    render: ->
      @$el.html @template @getRenderData()
      # render the subviews
      for viewId, view of @subViews
        html = view.render().el
        $(html).appendTo(@$el)
      # extend the ready rendered htlm element
      @afterRender()
      @


    destroy: ->
      @model?.off null, null, @

      # destroy all subviews
      for viewId, view of @subViews
        view.destroy()

      @$el.remove()

    # pass a final funktion, if u want to
    leave: (done = ->) ->
      @destroy()
      done()

   


  module.exports = NodeView