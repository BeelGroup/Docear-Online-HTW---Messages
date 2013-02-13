define ['routers/DocearRouter', 'views/RootNodeView', 'views/NodeView', 'views/HtmlView', 'models/Node', 'models/RootNode'],  (DocearRouter, RootNodeView, NodeView,HtmlView,NodeModel,RootNodeModel) ->  
  module = ->

  class MapController


    constructor:->
      @appendMindmapContainer()
      @appendZoomPanel()


    positionNodes:()->
      jsPlumb.reset()
      rootView = new RootNodeView @rootNode
      # remove old html elements
      $('.root').remove();
      # create and append new html 
      @$rootHtml = $(rootView.render().el).html()
      $("#mindmap").append @$rootHtml
      
      rootView.connectChildren()
      rootView.centerInContainer()
      rootView.refreshDom()
      jsPlumb.repaintEverything()


    loadMap: (mapId) ->
      console.log "call: loadMap #{mapId} (MapController)"
      href = jsRoutes.controllers.ControllerFactory.mindMap.map(mapId).url
      $.get(href, @createJSONMap, "json")


    createJSONMap: (data)=>
      #id, folded, nodeText, containerID, isHTML, xPos, yPos, hGap, shiftY, locked
      @rootNode = new RootNodeModel(data.root.id, false, data.root.nodeText, document.mindmapID ,data.root.isHtml, 0,0,0,0,false) 
      
      if data.root.leftChildren != undefined
        leftNodes = getRecursiveChildren(data.root.leftChildren)
        @rootNode.set 'leftChildren', leftNodes
      
      if data.root.rightChildren != undefined
        rightNodes = getRecursiveChildren(data.root.rightChildren)
        @rootNode.set 'rightChildren', rightNodes

      @positionNodes()
      @rootNode


    getRecursiveChildren = (childrenData)->
      children = []
      if childrenData.id != undefined && childrenData.id != null
        #id, folded, nodeText, isHTML, xPos, yPos, hGap, shiftY, locked
        newChild = new NodeModel(childrenData.id, childrenData.folded, childrenData.nodeText, childrenData.isHtml,0,0,0,0,false)
        children.push newChild
      else if childrenData != undefined
        for child in childrenData
          if child.nodeText != ""
            newChild = new NodeModel(child.id, child.folded, child.nodeText, child.isHtml,0,0,0,0,false)
            if child.children != undefined
              newChild.set 'children', getRecursiveChildren(child.children)
            children.push newChild
      children

    appendZoomPanel:()->
      zoomPanelView = Backbone.View.extend

        id: 'zoomPanel'
        tagName: 'div'
        className: 'zoomPanel'
        template: Handlebars.templates['ZoomPanel']

        events:
          "click #zoom-in"  : "zoomIn"
          "click #zoom-out" : "zoomOut"

        zoomIn:()->
          console.log "zoom in"

        zoomOut:()->
          console.log "zoom out"


        renderAndAppendTo:(id)->
          $("##{id}").append(@render().el)


        render:->
          @$el.html @template {zoomFactor: 0}
          @$el.css 'position', 'absolute'
          @$el.css 'left', '1%'
          @$el.css 'top', '1%'
          @

      newZoomPanelView = new zoomPanelView()
      newZoomPanelView.renderAndAppendTo document.mindmapContainer


    appendMindmapContainer:()->
      MindmapContainer = Backbone.View.extend
        id: document.mindmapID
        tagName: 'div'
        className: 'ui-draggable'

        afterAppend:()->
          @$el.draggable({
            cancel: "a.ui-icon, .node",
            containment: document.mindmapContainer,
            cursor: "move",
            handle: document.mindmapID
          });

        renderAndAppendTo:(id)->
          $("##{id}").append(@render().el)
          @afterAppend()

      newMindmapContainer = new MindmapContainer()
      newMindmapContainer.renderAndAppendTo(document.mindmapContainer)





    renderMap:(mapId)->
      ## first three entries currently filled in main.scala.html
      @loadMap(mapId)


  module.exports = MapController  