package raman.chatSystem.model

import raman.chatSystem.R
import raman.chatSystem.model.messages.DummyData

object Users
{
    fun getUsers(): List<User>
    {
        //val messages = DummyData.getDummyData()
        val users = ArrayList<User>()
        users.add(User("Raman", "+44", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Aras", "+63", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Muler", "+90", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Raskon", "+76", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Delegator", "+74", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Hakar", "+93", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Rima", "+15", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Somi", "+51", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        users.add(User("Sony", "+83", 7588838389,0, DummyData.getDummyData(), R.drawable.ic_user))
        return users
    }
}