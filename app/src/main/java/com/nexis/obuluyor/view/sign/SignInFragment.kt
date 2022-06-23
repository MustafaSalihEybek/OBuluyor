package com.nexis.obuluyor.view.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignInFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var navDirections: NavDirections

    private lateinit var txtUserEmail: String
    private lateinit var txtUserPassword: String

    private fun init(){
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        observeLiveData()

        sign_in_fragment_btnSignIn.setOnClickListener(this)
        sign_in_fragment_btnSignUp.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        signInViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        signInViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
                goToMainPage()
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.sign_in_fragment_btnSignIn -> signInUser()
                R.id.sign_in_fragment_btnSignUp -> Singleton.setPage(1)
            }
        }
    }

    private fun signInUser(){
        txtUserEmail = sign_in_fragment_editUserEmail.text.toString().trim()
        txtUserPassword = sign_in_fragment_editUserPassword.text.toString().trim()

        if (!txtUserEmail.isEmpty()){
            if (!txtUserPassword.isEmpty())
                signInViewModel.signInUser(txtUserEmail, txtUserPassword)
            else
                txtUserPassword.show(v, "Lütfen şifrenizi giriniz")
        } else
            txtUserEmail.show(v, "Lütfen email adresinizi giriniz")
    }

    private fun goToMainPage(){
        navDirections = SignFragmentDirections.actionSignFragmentToMainFragment()
        Navigation.findNavController(v).navigate(navDirections)
    }
}