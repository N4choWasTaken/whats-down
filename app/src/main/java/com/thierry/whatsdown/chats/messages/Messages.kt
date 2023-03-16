package com.thierry.whatsdown.chats.messages

import com.google.firebase.firestore.DocumentReference

class Messages(_content: String, _chat: DocumentReference, _user: String) {
    var content = _content
    var user = _chat
    var chat = _user
}
