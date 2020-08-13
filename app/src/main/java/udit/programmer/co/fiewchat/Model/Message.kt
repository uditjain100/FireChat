package udit.programmer.co.fiewchat.Model

class Message() {

    var senderID = ""
    var recieverID = ""
    var message = ""
    var isSeen = false
    var url = ""
    var messageID = ""

    constructor(
        senderID: String,
        recieverID: String,
        message: String,
        isSeen: Boolean,
        url: String,
        messageID: String
    ) : this() {
        this.senderID = senderID
        this.recieverID = recieverID
        this.message = message
        this.isSeen = isSeen
        this.url = url
        this.messageID = messageID
    }

}