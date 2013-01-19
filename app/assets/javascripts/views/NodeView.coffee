define ['views/AbstractNodeView'], (AbstractNodeView) ->
  module = ->
  
  class NodeView extends AbstractNodeView

    initialize: (model) ->
      super model


    recursiveRender: (parent, nodes)->
      $.each(nodes, (index, node)=>
        nodeView = new NodeView(node)
        $nodeHtml = $($(nodeView.render().el).html())
        $('#mindmap').append($nodeHtml)
        children = node.get 'children'
        if children != undefined
          @recursiveRender(nodeView, children)
      )

    
    adjustNodeHierarchy: (parent, children, treeIdentifier)->
      parentId = parent.get 'id'
      $parent = $('#'+parentId)
      
      console.log parent
      console.log children

      $.each(children, (index, node)=>
        childId = node.get 'id' 
        $child = $('#'+childId)
        $child.addClass(treeIdentifier)
        children = node.get 'children'
        if children != undefined
          @adjustNodeHierarchy(node, children)
        $parent.children('.children:first').append($child)
        connectNodes $parent, $child
      )
      
        
    centerNodeInContainer: (container, node)->
      containerWidth = $(container).width();
      containerHeight = $(container).width();
      nodeWidth = $(node).width()
      nodeHeight = $(node).width()
      $(node).css('left', containerWidth/2-nodeWidth/2)
      $(node).css('top', containerHeight/2-nodeHeight/2)
      
    getCenterCoordinates: ($element) ->
      left = $element.position().left + $element.width() / 2
      top = $element.position().top + $element.height() / 2
      top: top, left: left
  

    
    #
    # Refresh the mind map an reposition the dom elements
    #
    refreshDom: () ->
      root = $('.root:first')
      height = @_alignChildren root
      jsPlumb.repaintEverything()
      height
    
    _alignChildren: (element) ->
      horizontalSpacer = 20
      $children = $(element).children('.children').children('.node')
      elementHeight = $(element).outerHeight()
      elementWidth = $(element).outerWidth()
      heightOfChildren = {}
      parentCenterTop = @getCenterCoordinates($(element)).top
      totalChildrenHeight = 0
      
      currentLeftTop = 0
      currentRightTop = 0
      currentTop = 0
      if $children.length > 0
        for child in $children
          childHeight = @_alignChildren(child) 
          heightOfChildren[$(child).attr('id')] = childHeight
          totalChildrenHeight = totalChildrenHeight + childHeight + @verticalSpacer
        
        lastChild = null
        for child in $children
          $(child).css('border', "3px dotted #0000FF")
          if $(child).hasClass('leftTree')
            $(child).css("left", -$(child).outerWidth() - horizontalSpacer)
            $(child).css("top", currentLeftTop)
            currentLeftTop = currentLeftTop + heightOfChildren[$(child).attr('id')]
          else
            $(child).css("left", elementWidth + horizontalSpacer) 
            $(child).css("top", currentRightTop)
            currentRightTop = currentRightTop + heightOfChildren[$(child).attr('id')]
          lastChild = child
        currentTop = Math.max(currentRightTop, currentLeftTop) + heightOfChildren[$(lastChild).attr('id')]
        $(element).children('.children:first').css('top', -currentTop/2 + elementHeight/2)
      Math.max(currentTop, elementHeight)








    destroy: ->
      @model?.off null, null, @

      # destroy all subviews
      for viewId, view of @subViews
        view.destroy()

      @$el.remove()

    # pass a final function, if u want to
    leave: (done = ->) ->
      @destroy()
      done()


  module.exports = NodeView