package raman.chatSystem.fragments.phoneSignUp

import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import raman.chatSystem.R
import raman.chatSystem.databinding.FragmentCodeVerifyBinding

class View_CodeVerify(binding: FragmentCodeVerifyBinding, private val fragment: Fragment)
{
    val btnPhoneSubmit = binding.btnPhoneSubmit
    val btnCodeSubmit = binding.btnCodeSubmit
    val inputBoxPhone = binding.inputBoxPhone
    val inputBoxCode = binding.inputBoxCode
    val codeErrorMsg = binding.codeErrorMsg
    val resendCode = binding.resendCode
    val codeSentDescription = binding.codeSentDescription
    val networking = binding.networking.internetState
    val ip = binding.ipAddress

    private val phoneErrorMsg = binding.phoneErrorMsg
    private val panelPhone = binding.panelPhone
    private val panelCode = binding.panelCode
    private val call_country_codes = binding.callCodes

    //Determine whether phone or code panel is visible
    private var isPhoneStageComplete = false

    lateinit var drownDownList: Spinner


    init {
        //Show phone enter panel and hide code verification panel
        swapPanelVisibilities()
        //Setup country phone code dropdown
        dropdownBox()
    }

    fun swapPanelVisibilities()
    {
        if (!isPhoneStageComplete)
        {
            panelPhone.visibility = View.VISIBLE
            phoneErrorMsg.visibility = View.GONE
            panelCode.visibility = View.GONE
        }
        else
        {
            panelPhone.visibility = View.GONE
            codeErrorMsg.visibility = View.GONE
            panelCode.visibility = View.VISIBLE
        }
        isPhoneStageComplete = !isPhoneStageComplete
    }

    private fun dropdownBox()
    {
        drownDownList = call_country_codes

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(fragment.requireContext(), R.array.codes, R.layout.drop_down_text)

        adapter.setDropDownViewResource(R.layout.drop_down_text)

        drownDownList.adapter = adapter

        // on below line we are creating a variable to which we have to set our spinner item selected.
        val selection = "+44"

        // on below line we are getting the position of the item by the item name in our adapter.
        val spinnerPosition: Int = adapter.getPosition(selection)

        // on below line we are setting selection for our spinner to spinner position.
        drownDownList.setSelection(spinnerPosition)
    }

    fun setErrorMessage(text:String, type: String)
    {
        if(type.equals("phone", true))
        {
            phoneErrorMsg.visibility = View.VISIBLE
            phoneErrorMsg.text = text
            return
        }
        codeErrorMsg.visibility = View.VISIBLE
        codeErrorMsg.text = text
    }

    fun get_phone_input_value(): String
    {
        return inputBoxPhone.text.toString().trim()
    }

    fun get_code_input_value(): String
    {
        return inputBoxCode.text.toString().trim()
    }

    fun isCode_Input_Empty(): Boolean
    {
        return TextUtils.isEmpty(get_code_input_value())
    }

    fun isPhone_Input_Empty(): Boolean
    {
        return TextUtils.isEmpty(get_phone_input_value())
    }

    fun isWrongPhoneNumber() :Boolean
    {
        val userPhone = get_phone_input_value()
        var showMessage = true
        if(isPhone_Input_Empty())
            phoneErrorMsg.text = "Missing Valid Phone Number!"
        else if(userPhone.length < 10)
            phoneErrorMsg.text = "Missing Couple of Digits!"
        else if(userPhone.startsWith("0"))
            phoneErrorMsg.text = "Start without ZERO!"
        else
            showMessage = false
        if (showMessage)
            phoneErrorMsg.visibility = View.VISIBLE
        else
            phoneErrorMsg.visibility = View.GONE
        return showMessage
    }
}