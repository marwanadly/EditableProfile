package com.sparknetworks.editableprofile.ui.login


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.ui.login.viewModel.LoginViewModel
import com.sparknetworks.editableprofile.ui.login.viewModel.LoginViewModelFactory
import com.sparknetworks.editableprofile.ui.profile.ProfileActivity
import com.sparknetworks.editableprofile.utli.Common
import com.sparknetworks.editableprofile.utli.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber


class LoginFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val loginViewModelFactory : LoginViewModelFactory by instance()
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mView: View
    private lateinit var awesomeValidation: AwesomeValidation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        awesomeValidation = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel = ViewModelProvider(this,loginViewModelFactory).get(LoginViewModel::class.java)
        awesomeValidation.addValidation(mView.text_input_email, Patterns.EMAIL_ADDRESS, Common.EMAIL_ERROR)
        awesomeValidation.addValidation(mView.text_input_password, ".{6,}", Common.PASSWORD_ERROR)
        initView(mView)
    }

    private fun initView(view: View) {
        view.nav_register_bt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        view.login_btn.setOnClickListener {
            if(awesomeValidation.validate()) login(view)
            hideKeyboard()
        }
        loginViewModel.getOnSuccussLiveData().observe(viewLifecycleOwner, Observer {
            activity?.startActivity(Intent(activity, ProfileActivity::class.java))
            activity?.finish()
            //TODO("Check if profile existed for this user if not redirect to edit profile activity")
        })
        loginViewModel.getOnErrorLiveData().observe(viewLifecycleOwner, Observer {
            Timber.e(it)
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
        loginViewModel.getOnLoadingLiveData().observe(viewLifecycleOwner, Observer {
            view.login_loading.visibility = it
        })
    }

    private fun login(view: View) {
        val email = view.login_email.text.toString()
        val password = view.login_password.text.toString()
        loginViewModel.login(email = email, password = password)
    }

}
