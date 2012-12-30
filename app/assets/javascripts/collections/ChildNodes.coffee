define ['models/Node'], (Node) ->
  module = () ->

  class ChildNodes extends Backbone.Collection

    model: Node

    initialize: () ->
      super()


  module.exports = ChildNodes