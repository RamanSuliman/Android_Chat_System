package raman.chatSystem.model.messages

abstract class MessageCenter
{
    abstract val time: String
    abstract val message: String
    abstract val id: Int
    abstract val type: MessageType
}