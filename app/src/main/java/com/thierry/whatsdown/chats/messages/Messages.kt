package com.thierry.whatsdown.chats.messages

import com.google.firebase.firestore.DocumentReference
import com.thierry.whatsdown.User

class Messages(_content: String, _chat: DocumentReference, _user: DocumentReference) {
    var content = _content
    var user = _user
    var chat = _chat
}
