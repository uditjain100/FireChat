package udit.programmer.co.fiewchat.Model

class User() {

    var uid = ""
    var name = ""
    var phoneNumber = ""
    var email = ""
    var passWord = ""
    var userName = ""
    var profile = ""
    var cover = ""
    var status = ""
    var search = ""
    var facebook = ""
    var instagram = ""
    var website = ""

    constructor(
        uid: String,
        userName: String,
        name: String,
        phoneNumber: String,
        email: String,
        passWord: String
    ) : this() {
        this.uid = uid
        this.userName = userName
        this.name = name
        this.email = email
        this.passWord = passWord
        this.phoneNumber = phoneNumber
    }

}