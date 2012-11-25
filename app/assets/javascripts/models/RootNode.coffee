require ['models/AbstractNode'],  (AbstractNode) ->  

  class RootNode extends AbstractNode 
    init: (id, folded, nodeText, @leftchildren, @rightChildern) ->
      super id, folded, nodeText