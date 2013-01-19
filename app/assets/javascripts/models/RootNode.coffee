define ['models/Node'],  (Node) ->  
  module = ->
  
  class RootNode extends Node
    constructor: (id, folded, nodeText) ->
      super id, folded, nodeText
      @node = new Node(id, folded, nodeText)
      @leftchildren = []
      @rightChildern = []
      
  module.exports = RootNode