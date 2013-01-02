define ['models/Node', 'views/SyncedView', 'views/HtmlView'], (nodeModel, SyncedView, HtmlView) ->
  module = ->
  
  class NodeView extends SyncedView

    tagName: 'div',
    className: 'node'	
    subViews: {}
    template: Handlebars.templates['Node']



    fieldMap:
      '#nodeText': "nodeText"
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
      super()
      id: @model.get 'id'
      @model.bind "change:locked",@changeLockStatus , @ 
      
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
      'click .changeable': 'lockModel'
      'click .action-show': 'printModel'
      'click .action-change': 'modificateModel'
      'click .action-save': (-> @model.save(@model.saveOptions))
    
    lockModel: ->
      # will be replaced by username
      @model.lock 'me'
      console.log 'locked'

    changeLockStatus: ->
      if @model.get 'locked' 
        if (@model.get('lockedBy') != 'me')
          @$('.changeable').attr('disabled', 'disabled')
      else
        @$('.changeable').removeAttr('disabled')

    # TODO: implement
    fadeInButton: -> 
      console.log 'fade in "save changes" button and lock node on server'

    # [Debugging] 
    printModel: ->      
      console.log @model.toJSON()

    # [Debugging] model modification
    modificateModel: -> 
      @model.set 'nodeText', Math.random()   
      @model.set 'xPos', (@model.get 'xPos') + 20   
      @model.set 'yPos', (@model.get 'yPos') + 20
      if(@model.get 'locked')   
        @model.unlock()
      else
        @model.lock 'Mr. P'

      
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

    afterRender: ->
      @$el.append(@model.get 'purehtml')

    positioning: ->
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
        $(html).appendTo(@el)
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

    # pass a final function, if u want to
    leave: (done = ->) ->
      @destroy()
      done()


  module.exports = NodeView