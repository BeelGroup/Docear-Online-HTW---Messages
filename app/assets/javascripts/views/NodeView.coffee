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
    temporarayTemplate = '<div class="entry"><h1>id: {{id}}</h1><div class="body">folded: {{folded}} nodeText: {{nodeText}} graph: {{graph}}</div></div>'

      ## just for debugging
    innerStats = {title: "My New Post", body: "This is my first post!"}
    
    template: Handlebars.compile temporarayTemplate

    # a.k.a. constructor
    initialize: () ->

    # define events -> here u can pass informations to the model
    events: ->
    ##'click .element': alert 'click'

    # pass the data from the model to the template
    render: (model) ->
      @$el.html @template(model.toJSON())
      @

  # export da class instead of a da module, mtfckr
  module.exports = NodeView