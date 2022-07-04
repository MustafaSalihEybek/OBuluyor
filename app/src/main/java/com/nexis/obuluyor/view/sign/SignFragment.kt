package com.nexis.obuluyor.view.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.SignPagerAdapter
import com.nexis.obuluyor.util.Singleton
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_sign.*

class SignFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections

    private lateinit var signPagerAdapter: SignPagerAdapter
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = SignFragmentArgs.fromBundle(it).userId

            signPagerAdapter = SignPagerAdapter(childFragmentManager)
            signPagerAdapter.addFragment(SignInFragment())
            signPagerAdapter.addFragment(SignUpFragment())

            sign_fragment_viewPager.adapter = signPagerAdapter
            Singleton.mViewPager = sign_fragment_viewPager

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgClose -> backToPage(userId)
            }
        }
    }

    private fun backToPage(userId: Int){
        navDirections = SignFragmentDirections.actionSignFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}