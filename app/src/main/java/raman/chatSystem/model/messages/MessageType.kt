package raman.chatSystem.model.messages

enum class MessageType (val typeValue: Int)
{
    SENT_MESSAGE(1), RECEIVED_MESSAGE(2)
}