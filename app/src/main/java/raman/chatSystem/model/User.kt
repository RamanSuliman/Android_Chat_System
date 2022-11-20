package raman.chatSystem.model

import android.graphics.drawable.Drawable
import raman.chatSystem.model.messages.MessageCenter

class User(val username:String, val countryCode:String, val number:Long, var numOfUnreadMessages:Int, val messages: ArrayList<MessageCenter>, val picture:Int)
{
    fun getNumberAsString(): String = countryCode + number
}