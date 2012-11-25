require ['models/Node'],  (nodeModel) ->  
## representing one node
## example:
## view = new NodeView model: myNodeModel

  console.log nodeModel

  class NodeView extends Backbone.View

    taName: 'div',
    calssName: 'node'	

    ## template will be moved to /templates
    temporarayTemplate = '''
      <div class="node">
        <h1>{{nodeName}}</h1>
        {{#each details}}
          <li>{{this}}</li>
        {{/each}}
      </div>
    '''

    ## just for debugging
    stats = '''
    {
      nodeName: "My first node!",
      details: {
      detail1: "foo",
      detail2: "bar"
      }
    }
    '''
    template: Handlebars.compile temporarayTemplate

    # a.k.a. constructor
    initialize: () ->

    # define events -> here u can pass informations to the model
    events: ->
      'click .element': alert 'click'

    # pass the data from the model to the template
    render: (stats) ->
      @$el.html @template(@stats)
      @