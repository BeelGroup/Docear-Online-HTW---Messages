###
abstract class
###

define ['collections/ChildNodes'], (ChildNodes)->
  module = () ->

  class AbstractNode extends Backbone.Model 

    constructor: (id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked) ->
      super()    
      @set 'id', id
      @set 'folded', folded
      @set 'nodeText', nodeText
      @set 'isHTML', isHTML
      @set 'xPos', xPos
      @set 'yPos', yPos
      @set 'hGap', hGap
      @set 'shiftY', shiftY
      @set 'locked', locked
      ## THROW events on all (also possible: save/update/change)
      #@on 'all', (event) -> console.log "Event: " + event
      @sup = AbstractNode.__super__
      children = new ChildNodes()

    # will be set to /map/json/id, when fetch() or update() will be called
    urlRoot: '/map/json/' #TODO replace with jsRoutes command

    lock: (lockedBy) ->
      @set 'lockedBy', lockedBy
      @set 'locked', true
 

    unlock: ->
      @set 'locked', false

    # status messages for update
    saveOptions:
      success: (model) ->
        console.log "Node with id '#{model.id}' was updated to the server."
      error: (model, response) ->
        console.log "Error while saving Node with id '#{model.id}': #{response.status} #{response.statusText} (path was #{model.urlRoot})"

    fetchOptions:
      success: (model) ->
        console.log "Node with id '#{model.id}' was fetched from the server."
      error: (model, response) ->
        console.log "Error while fetching Node with id '#{model.id}': #{response.status} #{response.statusText} (path was #{model.urlRoot})"
    
    destroyOptions:
      success: ->
        editor.log "Node has been deleted from the permanent record."
      error: (model, response) ->
        editor.log "Error: #{response.status} #{response.statusText}"

    

      

  module.exports = AbstractNode