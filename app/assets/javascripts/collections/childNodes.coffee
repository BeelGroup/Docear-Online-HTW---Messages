define ['models/Node'] (Node) ->
  module = () ->

  class childNodes extends Backbone.Collection

    model: Node

    initialize: () ->
      super()


  module.export = childNodes

