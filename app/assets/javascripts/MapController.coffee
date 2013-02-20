define ['routers/DocearRouter', 'views/RootNodeView', 'views/NodeView', 'views/HtmlView', 'models/Node', 'models/RootNode'],  (DocearRouter, RootNodeView, NodeView,HtmlView,NodeModel,RootNodeModel) ->  
  module = ->

  class MapController


    constructor:->
      @addCanvas()
      @appendZoomPanel()
      @zoomAmount = 100


    positionNodes:()->
      jsPlumb.reset()
      @rootView = new RootNodeView @rootNode
      # remove old html elements
      @rootView.getElement().remove();
      # create and append new html 
      @$rootHtml = $(@rootView.render().el).html()
      $("##{document.canvasID}").append @$rootHtml      
      @rootView.connectChildren()
      @rootView.centerInContainer()
      @rootView.refreshDom()
      jsPlumb.repaintEverything()


    loadMap: (mapId) ->
      console.log "call: loadMap #{mapId} (MapController)"
      href = jsRoutes.controllers.ControllerFactory.mindMap.map(mapId).url
      $.get(href, @createJSONMap, "json")


    createJSONMap: (data)=>
      #id, folded, nodeText, containerID, isHTML, xPos, yPos, hGap, shiftY, locked
      @rootNode = new RootNodeModel(data.root.id, false, data.root.nodeText, document.canvasID ,data.root.isHtml, 0,0,0,0,false) 
      
      if data.root.leftChildren != undefined
        leftNodes = getRecursiveChildren(data.root.leftChildren)
        @rootNode.set 'leftChildren', leftNodes
      
      if data.root.rightChildren != undefined
        rightNodes = getRecursiveChildren(data.root.rightChildren)
        @rootNode.set 'rightChildren', rightNodes

      @positionNodes()
      @canvas.center()
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
          "click #zoom-in"     : "zoomIn"
          "click #zoom-out"    : "zoomOut"
          "click #zoom-center" : "zoomCenter"


        zoomIn:=>
          if(@zoomAmount+document.zoomStep <= 300)
            @zoomAmount += document.zoomStep
            @zoomPanel.zoom()

        zoomOut:=>
          if(@zoomAmount-document.zoomStep >= 50)
            @zoomAmount -= document.zoomStep
            @zoomPanel.zoom()

        zoom:=>
          node = @rootView.getElement()
          console.log "zoom:#{@zoomAmount}%"

          #CSS3:
          possibilities = document.body.style
          if($.inArray('WebkitTransform', possibilities) or 
             $.inArray('MozTransform', inpossibilities) or 
             $.inArray('OTransform', possibilities) or 
             $.inArray('transform', possibilities))
            zoomVar = @zoomAmount/100
            node.css
              '-moz-transform'    : "scale(#{zoomVar})"  #/* Firefox */
              '-webkit-transform' : "scale(#{zoomVar})"  #/* Safari and Chrome */
              '-ms-transform'     : "scale(#{zoomVar})"  #/* IE 9 */
              '-o-transform'      : "scale(#{zoomVar})"  #/* Opera */
          #node.css 'zoom', "#{@zoomAmount}%"
          #node.effect("scale", {percent:150, origin:['middle','center']}, 500)
          else
            console.log 'No CSS3'


        zoomCenter:()=>
          @zoomAmount = 100
          @zoomPanel.zoom()
          @rootView.centerInContainer()
          @canvas.center()


        renderAndAppendTo:(id)->
          $("##{id}").append(@render().el)


        render:->
          @$el.html @template {zoomFactor: 0}

          @$el.css 
            'position' : 'absolute'
            'left'     : '1%'
            'top'      : '1%'
          @

      @zoomPanel = new zoomPanelView()
      @zoomPanel.renderAndAppendTo document.viewportID


    addCanvas:()->
      MindmapCanvas = Backbone.View.extend
        id: document.canvasID
        tagName: 'div'
        className: 'ui-draggable'

        moreEvents:()=>
          $("##{document.canvasID}").mousewheel (event, delta, deltaX, deltaY)=>
            #event.stopPropagation()   # not IE
            #event.cancelBubble = true # IE
            if deltaY > 0 then @zoomPanel.zoomIn() else @zoomPanel.zoomOut()
            event.preventDefault() 

        afterAppend:()->
          @$el.draggable({
            cancel: "a.ui-icon, .node",
            containment: document.viewportID,
            cursor: "move",
            handle: document.canvasID
          });

        move:(x,y)->
          @$el.css 
           'left'  : "#{(@$el.css 'left')+x}px"
           'top'   : "#{(@$el.css 'top')+y}px"

        center:->
          xPos = document.canvasWidth/2 - $("##{document.viewportID}").width()/2
          yPos = document.canvasHeight/2 - $("##{document.viewportID}").height()/2
          @$el.css 
           'left'  : "#{-xPos}px",
           'top'   : "#{-yPos}px"

        renderAndAppendTo:(id)->
          $("##{id}").append(@render().el)

          @$el.css 
            'width' : "#{document.canvasWidth}px"
            'height': "#{document.canvasHeight}px"

          @center()
          @moreEvents()
          @afterAppend()

      @canvas = new MindmapCanvas()
      @canvas.renderAndAppendTo(document.viewportID)



    addMinimap:()->
      MindmapMinimap = Backbone.View.extend
        id: document.minimapCanvasID
        tagName: 'div'
        className: 'minimap-canvas'
        template: Handlebars.templates['Minimap']

        moreEvents:()=>
          $("##{document.canvasID}").mousewheel (event, delta, deltaX, deltaY)=>
            #event.stopPropagation()   # not IE
            #event.cancelBubble = true # IE
            if deltaY > 0 then @zoomPanel.zoomIn() else @zoomPanel.zoomOut()
            event.preventDefault() 

        afterAppend:()->
          @$el.draggable({
            cancel: "a.ui-icon, .node",
            containment: document.viewportID,
            cursor: "move",
            handle: document.canvasID
          });

        move:(x,y)->
          @$el.css 
           'left'  : "#{(@$el.css 'left')+x}px"
           'top'   : "#{(@$el.css 'top')+y}px"

        center:->
          xPos = document.canvasWidth/2 - $("##{document.viewportID}").width()/2
          yPos = document.canvasHeight/2 - $("##{document.viewportID}").height()/2
          @$el.css 
           'left'  : "#{-xPos}px",
           'top'   : "#{-yPos}px"

        renderAndAppendTo:(id)->
          $("##{id}").append(@render().el)

          @$el.css 
            'width' : "#{document.canvasWidth}px"
            'height': "#{document.canvasHeight}px"

          @center()
          @moreEvents()
          @afterAppend()

      @canvas = new MindmapCanvas()
      @canvas.renderAndAppendTo(document.viewportID)      



    renderMap:(mapId)->
      ## first three entries currently filled in main.scala.html
      @loadMap(mapId)


  module.exports = MapController  