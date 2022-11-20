package raman.chatSystem.fragments.phoneSignUp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import raman.chatSystem.databinding.FragmentCodeVerifyBinding

class CodeVerifyFragment : Fragment() {
    private lateinit var binding: FragmentCodeVerifyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vieww = View_CodeVerify(binding, this)
        Controller_CodeVerfiy(vieww, this, viewLifecycleOwner, findNavController())
    }
}
/**
    //Firebase
    private lateinit var verification: Verification

    //Determine whether phone or code panel is visible
    private var isPhoneStageComplete = false

    //Country code selected
    private var countryCode: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val view = View_CodeVerify(binding)
        val controller = Controller_CodeVerfiy(view,requireContext(),viewLifecycleOwner, findNavController())


        //Show phone enter panel and hide code verification panel
        swapPanelVisibilities()

        //Setting button action listeners
        setupUIActionListeners()

        verification = Verification(requireContext(), requireActivity(), this)

        checkInternetConnectionAuto()

        dropdownBox()


        //Start client
        val client = Client().start()

    }

    private fun checkInternetConnectionAuto()
    {
        //Network
        val connectionLiveData = NetworkConnectivity.getInstance(requireContext())!!
        //Run a manual network check
        binding.networking.internetState.text = if(connectionLiveData.checkInternetConnectionManual()) {
            binding.networking.internetState.setTextColor(ContextCompat.getColor(requireContext(),R.color.available))
            "Network Available !!"
        }else {
            binding.networking.internetState.setTextColor(ContextCompat.getColor(requireContext(),R.color.notAvailable))
            "No Internet :("
        }

        //Get Device IP address after checking the connection state
        binding.ipAddress.text = connectionLiveData.getDeviceIpAddress()

        connectionLiveData.observe(viewLifecycleOwner) { isNetworkAvailable ->
            binding.networking.internetState.text = if (isNetworkAvailable) {
                //Get Device IP address
                binding.ipAddress.text = connectionLiveData.getDeviceIpAddress()
                binding.networking.internetState.setTextColor(ContextCompat.getColor(requireContext(),R.color.available))
                "Network Available !!"
            }else {
                //Get Device IP address
                binding.ipAddress.text = connectionLiveData.getDeviceIpAddress()
                binding.networking.internetState.setTextColor(ContextCompat.getColor(requireContext(),R.color.notAvailable))
                "No Internet :("
            }
        }
    }

    private fun setupUIActionListeners()
    {
        binding.btnPhoneSubmit.setOnClickListener{
            val phone = binding.inputBoxPhone.text.toString().trim()
            if (!isWrongPhoneNumber(phone))
                verification.startPhoneNumberVerification(countryCode + phone)
        }
        binding.btnCodeSubmit.setOnClickListener{
            val code = binding.inputBoxCode.text.toString().trim()
            if(TextUtils.isEmpty(code))
                Toast.makeText(context, "Missing Verification Code", Toast.LENGTH_SHORT).show()
            else
                verification.verifyPhoneNumber(code)

        }
        binding.resendCode.setOnClickListener{
            val phone = binding.inputBoxPhone.text.toString().trim()
            if (TextUtils.isEmpty(phone))
                Toast.makeText(context, "Enter Phone Number", Toast.LENGTH_SHORT).show()
            else
                verification.resendVerificationCode(phone)
        }
    }

    private fun isWrongPhoneNumber(userPhone: String) :Boolean
    {
        var showMessage = true
        if(userPhone.isEmpty())
            binding.phoneErrorMsg.text = "Missing Valid Phone Number!"
        else if(userPhone.length < 10)
            binding.phoneErrorMsg.text = "Missing Couple of Digits!"
        else if(userPhone.startsWith("0"))
            binding.phoneErrorMsg.text = "Start without ZERO!"
        else
            showMessage = false
        if (showMessage)
            binding.phoneErrorMsg.visibility = View.VISIBLE
        else
            binding.phoneErrorMsg.visibility = View.GONE
        return showMessage
    }

    private fun swapPanelVisibilities()
    {
        if (!isPhoneStageComplete)
        {
            binding.panelPhone.visibility = View.VISIBLE
            binding.phoneErrorMsg.visibility = View.GONE
            binding.panelCode.visibility = View.GONE
        }
        else
        {
            binding.panelPhone.visibility = View.GONE
            binding.codeErrorMsg.visibility = View.GONE
            binding.panelCode.visibility = View.VISIBLE
        }
        isPhoneStageComplete = !isPhoneStageComplete
    }


    override fun onSuccess(verified: Boolean)
    {
        if(verified)
            findNavController().navigate(R.id.action_codeVerifyFragment_to_homeFragment)
    }

    override fun onFailure(reason: String, type: String)
    {
        setErrorMessage(reason, type, true)
    }

    override fun maxAttemptReached(verified: Boolean)
    {
        swapPanelVisibilities()
        binding.inputBoxPhone.setText("")
        binding.inputBoxCode.setText("")
    }

    override fun onCodeSent(verified: Boolean)
    {
        //Hide phone panel and show code panel
        swapPanelVisibilities()

        Toast.makeText(context, "Verification Code Sent...", Toast.LENGTH_SHORT).show()
        binding.codeErrorMsg.visibility = View.GONE
        binding.codeSentDescription.text = "Please type the verification code sent to ${binding.inputBoxPhone.text.toString().trim()}"
    }

    private fun setErrorMessage(text:String, type: String, isVisible: Boolean)
    {
        if(type.equals("phone", true))
        {
            binding.phoneErrorMsg.visibility = View.VISIBLE
            binding.phoneErrorMsg.text = text
            return
        }
        binding.codeErrorMsg.visibility = View.VISIBLE
        binding.codeErrorMsg.text = text
    }

    private fun dropdownBox()
    {
        val drownDownList: Spinner = binding.callCodes

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(requireContext(),
            R.array.codes, R.layout.drop_down_text)

        adapter.setDropDownViewResource(R.layout.drop_down_text)

        drownDownList.adapter = adapter

        // on below line we are creating a variable to which we have to set our spinner item selected.
        val selection = "+44"

        // on below line we are getting the position of the item by the item name in our adapter.
        val spinnerPosition: Int = adapter.getPosition(selection)

        // on below line we are setting selection for our spinner to spinner position.
        drownDownList.setSelection(spinnerPosition)

        drownDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long)
            {
                countryCode = drownDownList.selectedItem.toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
    }


 **/