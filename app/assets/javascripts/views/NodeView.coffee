define ['models/Node', 'views/SyncedView', 'views/HtmlView'], (nodeModel, SyncedView, HtmlView) ->
  module = ->
  
  class NodeView extends SyncedView

    tagName: 'div',
    className: 'node'	
    subViews: {}
    template: Handlebars.templates['Node']



    fieldMap:
      # example for an entry with his own methods
      '#body': "nodeText"
      '.node' :
        field: 'xPos'
        toModel: 'PosToModel'
        toForm: 'PosToForm'
      '.node' :
        field: 'yPos'
        toModel: 'PosToModel'
        toForm: 'PosToForm'

    # a.k.a. constructor
    initialize: (@model) ->
      super
      id: @model.get 'id'
      # render, when model was changed -> not in usage since sincedView was implemented
      #@model.on 'change', @render, @

    PosToModel: ->
      # TODO: Event will not be called on change
      @model.set 'xPos', @$el.css 'left'
      @model.set 'yPos', @$el.css 'top'

    PosToForm: ->
      @$el.animate({
        left: @model.get 'xPos'
        top: @model.get 'yPos'
      }, 500 );


    # define events -> here u can pass informations to the model
    events: =>
      'click .changeable': 'fadeInButton'
      'click .show': 'updateModel'
      'click .change': 'test'
      'click .save': (-> @model.save(@model.saveOptions))
    
    # TODO: implement
    fadeInButton: -> 
      console.log 'fade in "save changes" button and lock node on server'

    # Testing
    updateModel: ->      
      # update changed values to the model
      #@model.set 'id', $('.changeable').val() 
      console.log @model.toJSON()

    # Testing
    test: -> 
      @model.set 'nodeText', Math.random()   
      @model.set 'xPos', (@model.get 'xPos') + 20   
      @model.set 'yPos', (@model.get 'yPos') + 20   
      

    subView: (view, autoRender = false) ->
      # if model is set, use its id OR a unique random id
      viewId = view.model?.id or String(Math.random() * new Date().getTime())
      # add view to subviews
      @subViews[viewId] = view
      view

    getRenderData: ->
    # if the model is already set, parse it to json
      if @model?
        if @model.get('isHTML') == true
          # create subview  
          model = new Backbone.Model pureHtml: @model.get 'nodeText'
          myHtml = new HtmlView(model)
          # add it to the node
          @subView(myHtml)
        @model.toJSON()
    # otherwise pass an empty JSON
      else
        {}


    # extend the view with the jsPlumb div
    afterRender: ->
      @$el.append(@model.get 'purehtml')

    positioning: ->
      #@$el.css 'border', '1px'
     # @$el.css 'position', 'absolute'
     # @$el.css 'top', '150px'
     # @$el.css 'left', '150px'
      @$el.css({
      'position':'absolute', 
      'top': @calcYPosition() + 'px',
      'left': @calcXPosition() + 'px',
      });

    calcXPosition: ->
      @model.get 'xPos'

    calcYPosition: ->
      @model.get 'yPos'

    render: ->
      @$el.html @template @getRenderData()
      # render the subviews
      for viewId, view of @subViews
        html = view.render().el
        $(html).appendTo(@$el)
      # extend the ready rendered htlm element
      @afterRender()
      @positioning()
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