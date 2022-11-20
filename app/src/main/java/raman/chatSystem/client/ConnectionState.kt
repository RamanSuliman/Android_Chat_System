package raman.chatSystem.client

interface ConnectionState
{
    fun clientConnected()
    fun clientDisconnected()
    fun isServerDown()
    fun serverDisconnected()
    fun serverHasUnknownMatters()
}