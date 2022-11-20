package raman.chatSystem.model.verficiation

interface VerifyCallbacks
{
    fun onSuccess(verified: Boolean)
    fun onCodeSent(verified: Boolean)
    fun maxAttemptReached(verified: Boolean)
    fun onFailure(reason: String, type: String)
}