define ['models/Node'],  (Node) ->  
  module = ->
  
  class RootNode extends Node
    constructor: (id, folded, nodeText, containerID) ->
      super id, folded, nodeText
      @set 'containerID', containerID
      @node = new Node(id, folded, nodeText)
      @leftchildren = []
      @rightChildern = []
      
  module.exports = RootNode