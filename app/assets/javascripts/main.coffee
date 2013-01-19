require [],  () ->   
   
  if $("body").hasClass("login-page")
    $("#username").focus()
  
  loadUserMaps = ->
    $.ajax({
      type: 'GET',
      url: jsRoutes.controllers.ControllerFactory.mindMap.mapListFromDB().url,
      dataType: 'json',
      success: (data)->
        $selectMinmap = $('#select-mindmap')
        $.each(data, (index,value)->
          $selectMinmap.append """<li><a class="loadMap dropdown-toggle" href="#{jsRoutes.controllers.ControllerFactory.mindMap.map(value.mmIdOnServer).url}"> #{value.fileName}</a></li>"""
        )
        '/map/json/id'
    })
  loadUserMaps()
  
  
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
  
  ContactsRouter = Backbone.Router.extend({
    routes: {
      "loadMap/:mapId": "loadMap"
    },
   loadMap: (mapId)->
      href = "http://localhost:9000/map/json/"+mapId
      $(this).closest(".dropdown").children(".dropdown-toggle").click()
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
        
        #if data.root.rightChildren != undefined && data.root.rightChildren.nodeText != ""
        #  rightNode = new Node(data.root.rightChildren.nodeText)
        #  if data.root.rightChildren.nodeText == undefined
        #    rightNode.children = getRecursiveChildren(rightNode, data.root.rightChildren)
        #  current_mindmap.appendRight(rightNode)
        
        current_map_drawer = new MindMapDrawer(current_mindmap, $("#mindmap"))
        current_map_drawer.draw()
        
        foldedNodes = $('.node.folded')
        $(foldedNodes).children('.children').hide()
        $(foldedNodes).find("i.fold").toggleClass('icon-minus-sign')
        $(foldedNodes).find("i.fold").toggleClass('icon-plus-sign')
        
      $.get(href, recall, "json")
      false
  })

  contactsRouter = new ContactsRouter();

  Backbone.history.start();