package hubbub

class Tag {

    String name
    User user

    static hasMany = [posts: Post]
    static belongsTo = [user: User]

    static constraints = {
        name blank: false
    }
}
