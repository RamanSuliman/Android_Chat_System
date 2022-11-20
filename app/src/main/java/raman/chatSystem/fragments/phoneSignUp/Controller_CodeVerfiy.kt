package raman.chatSystem.fragments.phoneSignUp

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import raman.chatSystem.R
import raman.chatSystem.client.Client
import raman.chatSystem.client.ClientCommunicationManager
import raman.chatSystem.client.ClientController
import raman.chatSystem.model.LiveViewModel
import raman.chatSystem.model.internet.NetworkConnectivity
import raman.chatSystem.model.verficiation.Verification
import raman.chatSystem.model.verficiation.VerifyCallbacks

class Controller_CodeVerfiy(private val view: View_CodeVerify, private val fragment: Fragment,
                            private val viewLifecycleOwner: LifecycleOwner, private val navController: NavController): VerifyCallbacks
{
    //Firebase
    private var verification: Verification

    //Country code selected
    private var countryCode: String = ""

    //Accessed by ClientCommunicationManager to get live model from the context.
    var fragment_ = fragment

    init {
        //Setting button action listeners
        setupUIActionListeners()
        //Set dropdown action listener
        setup_countryCode_ActionListener()

        verification = Verification(fragment.requireContext(), fragment.requireActivity(), this)

        checkInternetConnectionAuto()

        ClientCommunicationManager(this)
    }

    private fun checkInternetConnectionAuto()
    {
        //Network
        val connectionLiveData = NetworkConnectivity.getInstance(fragment.requireContext())!!
        //Run a manual network check
        view.networking.text = if(connectionLiveData.checkInternetConnectionManual()) {
            view.networking.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.available))
            "Network Available !!"
        }else {
            view.networking.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.notAvailable))
            "No Internet :("
        }

        //Get Device IP address after checking the connection state
        view.ip.text = connectionLiveData.getDeviceIpAddress()

        connectionLiveData.observe(viewLifecycleOwner) { isNetworkAvailable ->
            view.networking.text = if (isNetworkAvailable) {
                //Get Device IP address
                view.ip.text = connectionLiveData.getDeviceIpAddress()
                view.networking.setTextColor(
                    ContextCompat.getColor(fragment.requireContext(), R.color.available))
                "Network Available !!"
            }else {
                //Get Device IP address
                view.ip.text = connectionLiveData.getDeviceIpAddress()
                view.networking.setTextColor(ContextCompat.getColor(fragment.requireContext(), R.color.notAvailable))
                "No Internet :("
            }
        }
    }

    private fun setupUIActionListeners()
    {
        view.btnPhoneSubmit.setOnClickListener{
            if (!view.isWrongPhoneNumber())
                verification.startPhoneNumberVerification(countryCode + view.get_phone_input_value())
        }

        view.btnCodeSubmit.setOnClickListener{
            if(view.isCode_Input_Empty())
                Toast.makeText(fragment.requireContext(), "Missing Verification Code", Toast.LENGTH_SHORT).show()
            else
                verification.verifyPhoneNumber(view.get_code_input_value())
        }

        view.resendCode.setOnClickListener{
            if (view.isPhone_Input_Empty())
                Toast.makeText(fragment.requireContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show()
            else
                verification.resendVerificationCode(view.get_phone_input_value())
        }
    }

    override fun onSuccess(verified: Boolean)
    {
        if(verified)
        {
            ViewModelProvider(fragment.requireActivity())[LiveViewModel::class.java].set_Client_Controller(ClientController(this))
            navController.navigate(R.id.action_codeVerifyFragment_to_homeFragment)
        }
    }

    override fun onFailure(reason: String, type: String)
    {
        view.setErrorMessage(reason, type)
    }

    override fun maxAttemptReached(verified: Boolean)
    {
        view.swapPanelVisibilities()
        view.inputBoxPhone.setText("")
        view.inputBoxCode.setText("")
    }

    override fun onCodeSent(verified: Boolean)
    {
        //Hide phone panel and show code panel
        view.swapPanelVisibilities()

        Toast.makeText(fragment.requireContext(), "Verification Code Sent...", Toast.LENGTH_SHORT).show()
        view.codeErrorMsg.visibility = View.GONE
        view.codeSentDescription.text = "Please type the verification code sent to 0${view.inputBoxPhone.text.toString().trim()}"
    }

    private fun setup_countryCode_ActionListener()
    {
        view.drownDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long)
            {
                countryCode = view.drownDownList.selectedItem.toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
    }

    /**
     * FOR TESTING PURPOSES
     */
    fun printout_server_status(status: String)
    {
        when(status)
        {
//            "connected" -> {Toast.makeText(fragment.requireContext(), "Server is life and connected", Toast.LENGTH_SHORT).show()}
//            "client_disconnected" -> {Toast.makeText(fragment.requireContext(), "Server is disconnected", Toast.LENGTH_SHORT).show()}
//            "down" -> {Toast.makeText(fragment.requireContext(), "Server is down now", Toast.LENGTH_SHORT).show()}
//            "server_disconnected" -> {Toast.makeText(fragment.requireContext(), "Server is disconnected", Toast.LENGTH_SHORT).show()}
//            else -> {Toast.makeText(fragment.requireContext(), "Unknown error in connecting with server", Toast.LENGTH_SHORT).show()}
            "connected" -> Log.d("Client_user", "Server is life and connected")
            "client_disconnected" -> Log.d("Client_user", "Server is disconnectedServer is life and connected")
            "down" -> Log.d("Client_user", "Server is life and connected")
            "server_disconnected" -> Log.d("Client_user", "Server is disconnected")
            else -> Log.d("Client_user", "Unknown error in connecting with server")
        }
    }
}