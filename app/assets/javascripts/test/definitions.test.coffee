test 'MindMap.append left', ->
  mindMap = new MindMap
  equal(0, mindMap.leftChildren.length, 'no children should be there')
  mindMap.appendLeft new Node "left 1"
  equal(1, mindMap.leftChildren.length, 'child should be there')

test 'MindMap.append right', ->
  mindMap = new MindMap
  equal(0, mindMap.rightChildren.length, 'no children should be there')
  mindMap.appendRight new Node "right 1"
  equal(1, mindMap.rightChildren.length, 'child should be there')