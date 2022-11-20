package raman.chatSystem.fragments.singleChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import raman.chatSystem.databinding.FragmentSingleChatBinding

class SingleChatFragment : Fragment()
{
    private lateinit var binding: FragmentSingleChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentSingleChatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val vieww = View_SingleChat(binding, this)
        Controller_SingleChat(vieww, this)
    }
}

/*


        //Set up live data
        setupLiveModel()

        binding.textName.text = "Raman"

        val recyclerView = binding.chatRecycleView
        //val messages = DummyData.getDummyData()
        val messages = user.value?.messages
        val adapter = RecyclerAdapter(messages!!)

        recyclerView.adapter = adapter
        // True if adapter changes cannot affect the size of the RecyclerView
        recyclerView.setHasFixedSize(true)
        //Make smooth scrolling
        recyclerView.isNestedScrollingEnabled = false
        //################ Add a liner between each view holder ##############
            //recyclerView.addItemDecoration(DividerItemDecoration(context, 1)) //Default method
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.liner))
        recyclerView.addItemDecoration(itemDecoration)
        //itemDecoration.onDrawOver()     //When drawing finished

        //Scroll to the last message when opening the chat
        recyclerView.scrollToPosition(messages.size - 1)

        binding.inputMessage.setOnClickListener{
            //Scroll to the last message when keyboard showed up
            recyclerView.scrollToPosition(messages.size - 1)
            Toast.makeText(context,  "Selected..", Toast.LENGTH_SHORT).show()
        }

        binding.sendMessage.setOnClickListener{
            if(binding.inputMessage.text.toString().isNotEmpty())
            {
                //Adding the new message into the adapter list
                adapter.addItem(Message(binding.inputMessage.text.toString(),
                    adapter.itemCount + 1, Calendar.getInstance().time.toString(), MessageType.SENT_MESSAGE))
                //Scroll to the last message
                recyclerView.scrollToPosition(messages.size - 1)
                //Empty the input text area
                binding.inputMessage.text.clear()
            }
        }


    private fun setupLiveModel()
    {
        myViewModel = ViewModelProvider(requireActivity())[LiveViewModel::class.java]
        user = myViewModel.selectedUser
        if(user.value != null)
            Toast.makeText(context, "Model is live", Toast.LENGTH_SHORT).show()
    }

 */