package raman.chatSystem.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import raman.chatSystem.R
import raman.chatSystem.adapter.chatHistory.ChatClickListener
import raman.chatSystem.adapter.chatHistory.RecyclerAdapter
import raman.chatSystem.client.ClientController
import raman.chatSystem.databinding.FragmentHomeBinding
import raman.chatSystem.model.LiveViewModel
import raman.chatSystem.model.User
import raman.chatSystem.model.Users
import raman.chatSystem.model.internet.NetworkConnectivity

class HomeFragment : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var myViewModel: LiveViewModel
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setupLiveModel()

        setUpChatHistory(binding.chatRecycleView)

        //Logout button
        binding.btnCallsHistory.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        checkInternetConnectionAuto()

                //TEMPPP
        //ViewModelProvider(requireActivity())[LiveViewModel::class.java].clientController.value?.connectClient()

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

        connectionLiveData.observe(viewLifecycleOwner) { isNetworkAvailable ->
            binding.networking.internetState.text = if (isNetworkAvailable) {
                binding.networking.internetState.setTextColor(ContextCompat.getColor(requireContext(),R.color.available))
                "Network Available !!"
            }else {
                binding.networking.internetState.setTextColor(ContextCompat.getColor(requireContext(),R.color.notAvailable))
                "No Internet :("
            }
        }
    }

    private fun setupLiveModel()
    {
        myViewModel = ViewModelProvider(requireActivity())[LiveViewModel::class.java]
//        if(myViewModel.selectedUser.value != null)
//            Toast.makeText(context, "Model is live", Toast.LENGTH_SHORT).show()
//        else
//            Toast.makeText(context, "Model is not live", Toast.LENGTH_SHORT).show()
    }

    private fun setUpChatHistory(recyclerView: RecyclerView)
    {
        //val users = Users.getUsers()
        val adapter = RecyclerAdapter(myViewModel.users.value!!)

        // TEST TO BE REMOVED
//        myViewModel.users.value!!.first().messages.forEach{
//            Log.d("Raman Chat:", it.message)
//        }

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        adapter.setClickListener(object : ChatClickListener{
            override fun onChatClickListener(user: User) {
                myViewModel.selectChatUser(user)
                Toast.makeText(requireContext(), "Selected user: ${myViewModel.selectedUser.value?.username}", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_homeFragment_to_single_Chat_Fragment)
            }
        })
    }

    private fun checkUser()
    {
        //Get current user
        val user = firebaseAuth.currentUser
        if(user == null)
            findNavController().navigate(R.id.action_homeFragment_to_codeVerifyFragment)
        else
        {
            val phone = user.phoneNumber
            binding.appName.text = phone
        }
    }
}

/*
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, firstFragment)
            commit()
        }

        childFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, firstFragment)
            commit()
        }

        binding.btnFragement1.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_callHistoryFragment)
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.frameLayoutFragment, firstFragment)
//                show(firstFragment)
//                addToBackStack(null)
//                commit()
//            }
        }

        binding.btnFragement2.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutFragment, secondFragment)
                //hide(firstFragment)
                addToBackStack(null)
                commit()
            }
 */