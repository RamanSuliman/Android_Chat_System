package raman.chatSystem.model.messages

object DummyData
{
    var counter = 0;
    fun getDummyData(): java.util.ArrayList<MessageCenter>
    {
        val messages = ArrayList<MessageCenter>()
        messages.add(Message("Hi, how you doing today?", counter++, "20-01-1997", MessageType.RECEIVED_MESSAGE))
        messages.add(Message("Am good and yourself?", counter++, "20-01-1997", MessageType.SENT_MESSAGE))
        messages.add(Message("Not bad thanks!", counter++, "20-01-1997", MessageType.RECEIVED_MESSAGE))
        messages.add(Message("What you doing later on?", counter++, "20-01-1997", MessageType.SENT_MESSAGE))
        messages.add(Message("Might go out to chill, and yourself?", counter++, "20-01-1997", MessageType.RECEIVED_MESSAGE))
        messages.add(Message("Well, hanging around the house", counter++, "20-01-1997", MessageType.SENT_MESSAGE))
        messages.add(Message("Cool, am coming to you then :)", counter++, "20-01-1997", MessageType.RECEIVED_MESSAGE))
        messages.add(Message("Nice, see you in a bit!", counter++, "20-01-1997", MessageType.SENT_MESSAGE))
        messages.add(Message("You too mate", counter++, "20-01-1997", MessageType.RECEIVED_MESSAGE))
        messages.add(Message("Calm, meet me at the town center when you get off the tram just turn right and walk straight", counter++, "20-01-1997", MessageType.SENT_MESSAGE))
        messages.add(Message("Try to come after traffic is down, you would get smashed otherwise!", counter++, "20-01-1997", MessageType.SENT_MESSAGE))
        messages.add(Message("Haha alright mate no pressure :)", counter++, "20-01-1997", MessageType.RECEIVED_MESSAGE))

        return messages
    }
}