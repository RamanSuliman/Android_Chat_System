package raman.chatSystem.model.verficiation

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class Verification(private val context: Context, private val activity: Activity, val callBack: VerifyCallbacks)
{
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mVerficationId: String
    private  lateinit var firebaseAuth: FirebaseAuth

    private val TAG = "MAIN_TAG"

    //Wrong Attempt Counter
    private var counter = 4

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    init {
        setup_dialog()
        setup_Firebase()
    }

    private fun setup_dialog()
    {
        //Progress Dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun setup_Firebase()
    {
        //Set firebase
        firebaseAuth = FirebaseAuth.getInstance()

        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            //This call back invokes in two situations:
            // 1- Instant verification, in some cases phone number is instantly verified without sending.
            // 2- Auto-retrieval, on some devices Google Play service automatically detects the incoming SMS
            // and perform verification without user interaction
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential)
            {
                Log.d(TAG, "onVerificationCompleted")
                //signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            // Invoked if invalid request is made, ex the phone number format is not valid
            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Log.d(TAG, "onVerificationFailed: ${e.message}")
                callBack.onFailure("The country code did not match the given phone format!", "phone")
            }

            // Code is sent and now waiting the user to enter the code then construct a credential by combining
            // the code with a verification ID
            override fun onCodeSent(verficationID: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d(TAG, "OnCodeSent: $verficationID")
                mVerficationId = verficationID
                forceResendingToken = token
                progressDialog.dismiss()

                callBack.onCodeSent(true)
            }
        }
    }

    fun startPhoneNumberVerification(phone: String)
    {
        Log.d(TAG, "startPhoneNumberVerification: $phone")

        progressDialog.setMessage("Verifying Phone Number...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallBacks)
            .setActivity(activity)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendVerificationCode(phone: String)
    {
        Log.d(TAG, "resendVerificationCode: $phone -Token: $forceResendingToken")

        progressDialog.setMessage("Verifying Phone Number...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallBacks)
            .setActivity(activity)
            .setForceResendingToken(forceResendingToken)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        callBack.onCodeSent(true)
        counter = 4
    }

    fun verifyPhoneNumber(code:String)
    {
        Log.d(TAG, "verifyPhoneNumber: $mVerficationId -Code: $code")

        progressDialog.setMessage("Verifying Code...")
        progressDialog.show()

        val credential = PhoneAuthProvider.getCredential(mVerficationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential)
    {
        Log.d(TAG, "signInWithPhoneAuthCredential")

        progressDialog.setMessage("Logging in")

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                //Login success
                progressDialog.dismiss()
                val phone = firebaseAuth.currentUser?.phoneNumber
                Toast.makeText(context, "$phone", Toast.LENGTH_SHORT).show()
                //Start the home fragment
                callBack.onSuccess(true)
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                checkIfAttemptMaxed()
                callBack.onFailure("Code is wrong, try again you have $counter ${if(counter > 1) "attempts." else "attempt."}", "code")
            }
    }

    private fun checkIfAttemptMaxed()
    {
        counter--
        if(counter < 1)
        {
            counter = 4
            callBack.maxAttemptReached(true)
        }

    }
}