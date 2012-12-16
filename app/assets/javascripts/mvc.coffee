require ['views/NodeView', 'models/Node'],  (NodeView, NodeModel) ->  

  model = new NodeModel('4711','0','My awesome node!','Da graph','1')
  view = new NodeView(model)

  model.set 'nodeText', 'my new nodetext'

  ## PUSH/PUT/PULL operations
  #save
  #model.save null, model.saveOptions
  #update
  #model.save 'nodeText', 'my new node text', model.saveOptions
  #delete
  #model.destroy model.destroyOptions
  Blogpost = Backbone.Model.extend
  defaults:
    subject: "More Backbone!"
    message: "+1"
    url: '/api/feedback/suggestions'
    
  Blogposts = Backbone.Collection.extend
    url: '/api/feedback/suggestions'

    
  posts = new Blogposts()  
  
  
  options =
    success: (model) ->
      editor.log "Our Suggestion has persistent id '#{model.id}'."
    error: (model, response) ->
      editor.log "Server error: #{response.status} #{response.statusText} #{response.responseText}"

    
  # create and post to the server
  #posts.create subject: "thanks 4 the fish!", message: "+1", options


  TestModel = Backbone.Model.extend
  
  posts.add subject: "i 00 like cookies", message: "a lot"
  posts.add subject: "i 01 like cookies", message: "a lot"
  posts.add subject: "i 02 like cookies", message: "a lot"
  posts.add subject: "i 03 like cookies", message: "a lot"
  posts.add subject: "i 04 like cookies", message: "a lot"
  posts.add subject: "i 05 like cookies", message: "a lot"

  func = -> editor.log 'sync'

  #what = Backbone.sync( func, posts, options)

  console.log posts.toJSON()

  #editor.log posts.at(0).get 'subject'

  $("#mvc").append(view.render(model).el)

