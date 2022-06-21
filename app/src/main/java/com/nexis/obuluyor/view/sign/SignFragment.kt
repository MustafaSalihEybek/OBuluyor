package com.nexis.obuluyor.view.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.SignPagerAdapter
import kotlinx.android.synthetic.main.fragment_sign.*

class SignFragment : Fragment() {
    private lateinit var v: View

    private lateinit var signPagerAdapter: SignPagerAdapter

    private fun init(){
        signPagerAdapter = SignPagerAdapter(childFragmentManager)

        signPagerAdapter.addFragment(SignInFragment())
        signPagerAdapter.addFragment(SignUpFragment())

        sign_fragment_viewPager.adapter = signPagerAdapter
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
}