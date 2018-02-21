package hubbub

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class TagSpec extends Specification implements DomainUnitTest<Tag> {

    def "Exercise tagging several posts with various tags"() {

        given: "A user with a set of tags"
        def user = new User(loginId: 'torikul', password: '123456')
        def tagGroovy = new Tag(name: 'groovy')
        def tagGrails = new Tag(name: 'grails')
        user.addToTags(tagGroovy)
        user.addToTags(tagGrails)
        user.save(failOnError: true)

        when: "The user tags two fresh posts"
        def groovyPost = new Post(content: "A groovy post")
        user.addToPosts(groovyPost)
        groovyPost.addToTags(tagGroovy)

        def bothPost = new Post(content: "A groovy and grails post")
        user.addToPosts(bothPost)
        bothPost.addToTags(tagGroovy)
        bothPost.addToTags(tagGrails)

        then: "Checking tag results"
        user.tags*.name.sort() == ['grails', 'groovy']

        1 == groovyPost.tags.size()
        2 == bothPost.tags.size()

    }

}
