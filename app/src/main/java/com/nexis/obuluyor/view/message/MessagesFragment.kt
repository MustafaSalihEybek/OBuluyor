package com.nexis.obuluyor.view.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.CustomFragmentPagerAdapter
import com.nexis.obuluyor.util.AppUtils
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_messages.*

class MessagesFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections

    private var userId: Int = -1
    private lateinit var pagerAdapter: CustomFragmentPagerAdapter

    private fun init(){
        arguments?.let {
            userId = MessagesFragmentArgs.fromBundle(it).userId

            pagerAdapter = CustomFragmentPagerAdapter(childFragmentManager)
            pagerAdapter.addFragment(SenderMessagesFragment(userId, "sender"), "Gönderilen Mesajlar")
            pagerAdapter.addFragment(ReceiverMessagesFragment(userId, "receiver"), "Alınan Mesajlar")

            messages_fragment_viewPager.adapter = pagerAdapter
            messages_fragment_tabLayout.setupWithViewPager(messages_fragment_viewPager)

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)

            AppUtils.setLogoTint(custom_toolbar_imgLogo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id){
                R.id.custom_toolbar_imgClose -> backToPage(userId)
            }
        }
    }

    private fun backToPage(userId: Int){
        navDirections = MessagesFragmentDirections.actionMessagesFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}