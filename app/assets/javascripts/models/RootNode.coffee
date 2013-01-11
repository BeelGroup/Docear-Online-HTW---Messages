define ['models/Node'],  (NodeModel) ->  
  module = ->
  
  class RootNode extends NodeModel
    constructor: (id, folded, nodeText) ->
      super id, folded, nodeText
      @node = new NodeModel(id, folded, nodeText)
      @leftchildren = []
      @rightChildern = []
      
  module.exports = RootNode