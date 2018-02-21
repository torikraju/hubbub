package hubbub

class Post {
    String content
    Date dateCreated

    static belongsTo = [user: User]
    static hasMany = [tags: Tag]

    static constraints = {
        content blank: false
        sort dateCreated: 'desc'
    }
}
