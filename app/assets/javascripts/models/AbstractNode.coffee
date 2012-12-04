###
abstract class
###

define ->
  module = () -> 

  class AbstractNode extends Backbone.Model 
    constructor: (id, folded, nodeText, graph) ->
      # call constructeor of the parent class ... it won't be call automatically... its not java... its the wildness, b
      super()
      @set 'id', id
      @set 'folded', folded
      @set 'nodeText', nodeText
      @set 'graph', graph

  module.exports = AbstractNode