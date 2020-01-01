package com.sparknetworks.editableprofile.ui.register


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.ui.profile.EditProfileActivity
import com.sparknetworks.editableprofile.ui.register.viewModel.RegisterViewModel
import com.sparknetworks.editableprofile.ui.register.viewModel.RegisterViewModelFactory
import com.sparknetworks.editableprofile.utli.Common
import com.sparknetworks.editableprofile.utli.hideKeyboard
import kotlinx.android.synthetic.main.fragment_register.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class RegisterFragment : Fragment() , KodeinAware {

    override val kodein by kodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var awesomeValidation: AwesomeValidation
    private lateinit var registerButton: Button
    private lateinit var mView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        awesomeValidation = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        mView = view
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerViewModel = ViewModelProvider(this,registerViewModelFactory).get(RegisterViewModel::class.java)
        initView()
        awesomeValidation.addValidation(mView.text_input_email, Patterns.EMAIL_ADDRESS, Common.EMAIL_ERROR)
        awesomeValidation.addValidation(mView.text_input_password, ".{6,}", Common.PASSWORD_ERROR)
        awesomeValidation.addValidation(mView.text_input_confirm_password, mView.text_input_password, Common.CONFIRM_PASSWORD_ERROR)
        registerButton.setOnClickListener { onRegisterClick() }
    }

    private fun onRegisterClick() {
        val email = mView.register_email.text.toString()
        val password = mView.register_password.text.toString()
        if (awesomeValidation.validate()) registerViewModel.register(email = email, password = password)
        hideKeyboard()
    }

    private fun initView() {
        mView.back_btn.setOnClickListener { findNavController().popBackStack() }
        registerButton = mView.register_btn
        registerViewModel.getOnSuccussLiveData().observe(viewLifecycleOwner, Observer<String> {
                val editProfileIntent = Intent(activity, EditProfileActivity::class.java)
                editProfileIntent.putExtra(
                    Common.ACTIVITY_NAME_EXTRA,
                    Common.REGISTER_FRAGMENT_EXTRA
                )
                activity?.startActivity(editProfileIntent)
                activity?.finish()
        })
        registerViewModel.getOnErrorLiveData().observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
        registerViewModel.getOnLoadingLiveData().observe(viewLifecycleOwner, Observer {
            mView.register_loading.visibility = it
        })
    }


}
