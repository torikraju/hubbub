package hubbub

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class UserSpec extends Specification implements DomainUnitTest<User> {

    def "Saving our first user to the database"() {
        given: "A brand new user"
        def torik = new User(loginId: 'torik', password: '123456')

        when: "the user is saved"
        torik.save()

        then: "it saved successfully and can be found in the database"
        torik.errors.errorCount == 0
        torik.id != null
        User.get(torik.id).loginId == torik.loginId

    }

    def "Updating a saved user changes its properties"() {

        given: "An existing user"
        def existingUser = new User(loginId: 'raju', password: '123456')
        existingUser.save(failOnError: true)

        when: "A property is changed"
        def foundUser = User.get(existingUser.id)
        foundUser.password = 'Password'
        foundUser.save(failOnError: true)

        then: "The change is reflected in the database"
        User.get(existingUser.id).password == 'Password'

    }

    def "Deleting an existing user removes it from the database"() {

        given: "An existing user"
        def user = new User(loginId: 'maruf', password: '123456')
        user.save(failOnError: true)

        when: "The user is deleted"
        def foundUser = User.findWhere(loginId: user.loginId)
        foundUser.delete(flush: true)

        then: "The user is removed from the database"
        !User.exists(foundUser.id)

    }

    def "Saving a user with invalid properties causes an error"() {

        given: "A user which fails several field validations"
        def user = new User(loginId: 'torik', password: 'tiny')

        when: "The user is validated"
        user.validate()

        then: "matching the errors"
        user.hasErrors()

        "size.toosmall" == user.errors.getFieldError("password").code
        "tiny" == user.errors.getFieldError("password").rejectedValue
/*        "url.invalid" == user.errors.getFieldError("homepage").code
        "not-a-url" == user.errors.getFieldError("homepage").rejectedValue*/
        !user.errors.getFieldError("loginId")


    }

    def "Recovering from a failed save by fixing invalid properties"() {

        given: "A user that has invalid properties"
        def torikul = new User(loginId: 'torikul', password: 'torikul')
        assert torikul.save() == null
        assert torikul.hasErrors()

        when: "We fix the invalid properties"
        torikul.password = "password"


        then: "The user saves and validates fine"
        torikul.validate()
        !torikul.hasErrors()
        torikul.save()
    }

    def "Ensure a user can follow other users"() {

        given:"A set of baseline users"
        def torik = new User(loginId: 'torik', password: '123456').save()
        def raju = new User(loginId: 'raju', password: '123456').save()
        def maruf = new User(loginId: 'maruf', password: '123456').save()

        when: "torik follows raju & maruf, and maruf follows raju"
        torik.addToFollowing(raju)
        torik.addToFollowing(maruf)
        maruf.addToFollowing(raju)

        then: "Follower counts should match following people"
        2 == torik.following.size()
        1 == maruf.following.size()

    }
}
