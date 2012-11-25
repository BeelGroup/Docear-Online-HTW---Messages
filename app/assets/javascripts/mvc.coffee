



$ ->
  $("#mvc").text("inserted via jQuery")



###################
#  test examples  #
###################

## show stack trace
chai.Assertion.includeStack = true; 
foo = 'foo'

## assert
chai.assert.equal(foo, 'foo')
chai.assert.typeOf(foo, 'string', 'foo is a string')

## expect
chai.expect(foo).to.be.a('string')
chai.expect(foo).to.equal('foo')
chai.expect(foo).to.have.length(3)

## should
should = chai.should()
foo.should.be.a('string')
foo.should.equal('foo')
foo.should.have.length(3)