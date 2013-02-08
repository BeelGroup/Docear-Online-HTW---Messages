define ['views/RootNodeView', 'views/NodeView', 'views/HtmlView', 'models/Node', 'models/RootNode'],  (RootNodeView, NodeView,HtmlView,NodeModel,RootNodeModel) ->
  module = ->
  
  class DocearRouter extends Backbone.Router

    routes: "loadMap/:mapId": "loadMap"

    loadMap: (mapId) ->
      href = jsRoutes.controllers.ControllerFactory.mindMap.map(mapId).url
      recall = (data)->
        $("#mindmap").html("")
        jsPlumb.reset()
        current_mindmap = new MindMap(data.root.nodeText)
        
        if data.root.leftChildren != undefined
          leftNode = getRecursiveChildren(data.root.leftChildren)
          for n in leftNode
            current_mindmap.appendLeft(n)
        
        if data.root.rightChildren != undefined
          rightNode = getRecursiveChildren(data.root.rightChildren)
          for n in rightNode
            current_mindmap.appendRight(n)

        current_map_drawer = new MindMapDrawer(current_mindmap, $("#mindmap"))
        current_map_drawer.draw()
        
        foldedNodes = $('.node.folded')
        $(foldedNodes).children('.children').hide()
        $(foldedNodes).find("i.fold").toggleClass('icon-minus-sign')
        $(foldedNodes).find("i.fold").toggleClass('icon-plus-sign')
        
      $.get(href, recall, "json")

      false


    getRecursiveChildren = (childrenData)->
      children = []
      if childrenData.id != undefined && childrenData.id != null
        newChild = new Node(childrenData.nodeText)
        children.push newChild
      else if childrenData != undefined
        for child in childrenData
          if child.nodeText != ""
            newChild = new Node(child.nodeText)
            newChild.folded = child.folded == "true"
            if child.children != undefined
              newChild.children = getRecursiveChildren(child.children)
            children.push newChild
      children

  module.exports = DocearRouter