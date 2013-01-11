require ['views/NodeView', 'views/HtmlView', 'models/Node', 'models/RootNode', 'MindMapPositioner'],  (NodeView,HtmlView,NodeModel,RootNodeModel,Positioner) ->  

    initializeJsPlumb()
    
    recursiveRender = (parent, nodes)->
      $.each(nodes, (index, node)->
        nodeView = new NodeView(node)
        $nodeHtml = $($(nodeView.render().el).html())
        $('#mindmap').append($nodeHtml)
        children = node.get 'children'
        if children != undefined
          recursiveRender(nodeView, children)
      )
    generateViews = (rootNode)->
      rootView = new NodeView(rootNode)
      recursiveRender rootView, rootNode.get 'rightChildren'
      recursiveRender rootView, rootNode.get 'leftChildren'
      $rootHtml = $(rootView.render().el).html()
      #recursiveRender $rootHtml, rootNode.get 'rightChildren'
      $("#mindmap").append($rootHtml)
      
    positionNodes = (rootNode)->
      
      
    
    right1 = new NodeModel('ID_1',false,  'right 1')
    right1_1 = new NodeModel('ID_5',false,  'right 1.1')
    right1_2 = new NodeModel('ID_6',false,  'right 1.2')
    
    right1_3 = new NodeModel('ID_2',false,  'right 1.3')
    right1_3_1 = new NodeModel('ID_3',false,  'right 1.3.1')
    right1_3_2 = new NodeModel('ID_4',false,  'right 1.3.2')
    
    right1_3.set 'children', [right1_3_1, right1_3_2], silent: true
    right1.set 'children', [right1_1, right1_2, right1_3], silent: true
    
    
    right2 = new NodeModel('ID_7',false,  'right 2')
    right3 = new NodeModel('ID_8',false, "right 3<br><img src='/assets/images/docear/logo/main-logo.png' style='height: 68px; width: 350px' />", true)
    
    left1 = new NodeModel('ID_9',false,  'right 1')
    left1_1 = new NodeModel('ID_10',false,  'right 1.1')
    left1_1_1 = new NodeModel('ID_11',false,  'right 1.1.1')
    left1_1_2 = new NodeModel('ID_12',false,  'right 1.1.2')
    left1_1.set 'children', [left1_1_1, left1_1_2], silent: true
    
    
    left1_2 = new NodeModel('ID_13',false,  'right 1.2')
    left1_2_1 = new NodeModel('ID_14',false,  'right 1.2.1')
    left1_2_2 = new NodeModel('ID_15',false,  'right 1.2.2')
    left1_2_3 = new NodeModel('ID_16',false,  'right 1.2.3')
    left1_2.set 'children', [left1_2_1, left1_2_2, left1_2_3], silent: true
    
    
    rootNode = new RootNodeModel("ID_0", false, "root")
    rootNode.set 'rightChildren', [right1, right2, right3]
    rootNode.set 'leftChildren', [left1]

    generateViews rootNode
    mindMapPositioner = new Positioner($('#mindmap'), rootNode)

    
    
  
    getRecursiveChildren = (childrenData)->
      children = []
      if childrenData.id != undefined && childrenData.id != null
        newChild = right1 = new NodeModel(childrenData.id, childrenData.folded == "true",  childrenData.nodeText)
        children.push newChild
      else if childrenData != undefined
        for child in childrenData
          if child.nodeText != ""
            newChild = right1 = new NodeModel(child.id, child.folded == "true",  child.nodeText)
            if child.children != undefined
              newChild.set 'children', getRecursiveChildren(child.children), silent: true
            children.push newChild
      children
    
    $('body').on('click', ".loadMap", 
      ->
      href = $(this).attr("href")
      $(this).closest(".dropdown").children(".dropdown-toggle").click()
      recall = (data)->
        $("#mindmap").html("")
        jsPlumb.reset()
        
        rootNode = new RootNodeModel(data.root.id, data.root.folded == "true", data.root.nodeText)
        
        if data.root.leftChildren != undefined
          leftNodes = getRecursiveChildren(data.root.leftChildren)
          rootNode.set 'leftChildren', leftNodes
        
        if data.root.rightChildren != undefined
          rightNodes = getRecursiveChildren(data.root.rightChildren)
          rootNode.set 'rightChildren', rightNodes
        
        generateViews rootNode
        mindMapPositioner = new Positioner($('#mindmap'), rootNode)
        
        foldedNodes = $('.node.folded')
        $(foldedNodes).children('.children').hide()
        $(foldedNodes).find("i.fold").toggleClass('icon-minus-sign')
        $(foldedNodes).find("i.fold").toggleClass('icon-plus-sign')
        
      $.get(href, recall, "json")
      false
    )