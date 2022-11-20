package raman.chatSystem.model.messages

class Message(override val message: String, override val id: Int,
              override val time: String, override val type: MessageType): MessageCenter()
