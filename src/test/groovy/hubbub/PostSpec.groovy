package hubbub

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PostSpec extends Specification implements DomainUnitTest<Post> {

    def "Adding posts to user links post to user"() {
        given: "A brand new user"
        def user = new User(loginId: 'torik', password: '123456')
        user.save(failOnError: true)

        when: "Several posts are added to the user"
        user.addToPosts(new Post(content: "First post... W00t!"))
        user.addToPosts(new Post(content: "Second post..."))
        user.addToPosts(new Post(content: "Third post..."))

        then: "The user has a list of posts attached"
        3 == User.get(user.id).posts.size()

    }

    def "Ensure posts linked to a user can be retrieved"() {

        given: "A user with several posts"
        def user = new User(loginId: 'torik', password: '123456')
        user.addToPosts(new Post(content: 'First'))
        user.addToPosts(new Post(content: 'Second'))
        user.addToPosts(new Post(content: 'Third'))
        user.save(failOnError: true)

        when: "The user is retrieved by their id"
        def foundUser = User.get(user.id)
        def sortedPost = foundUser.posts.collect { it.content }.sort()

        then: "The post appear on the retrieved user"
        sortedPost == ['First', 'Second', 'Third']

    }

}
