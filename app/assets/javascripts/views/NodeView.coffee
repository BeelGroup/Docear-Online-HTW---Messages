#define ['models/Node', 'text!views/templates/Node.handlebars'], (nodeModel, nodeTemplate) ->
define ['models/Node'], (nodeModel) ->
  module = ->
  module.create = (properties) ->
    return new NodeView(properties)
  ## representing one node
  ## example: 
  ## view = new NodeView

  class NodeView extends Backbone.View

    taName: 'div',
    calssName: 'node'	

      ## template will be moved to /templates
    temporarayTemplate = '<div class="entry"><h1>{{title}}</h1><div class="body">{{body}}</div></div>'

      ## just for debugging
    innerStats = {title: "My New Post", body: "This is my first post!"}
    
    template: Handlebars.compile temporarayTemplate

    # a.k.a. constructor
    initialize: () ->

    # define events -> here u can pass informations to the model
    events: ->
    ##'click .element': alert 'click'

    # pass the data from the model to the template
    render: (stats) ->
      @$el.html @template(innerStats)
      @


  module