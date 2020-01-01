package com.sparknetworks.editableprofile.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.data.fixedValues.model.City
import com.sparknetworks.editableprofile.data.profile.model.Profile
import com.sparknetworks.editableprofile.ui.profile.viewModel.EditProfileViewModel
import com.sparknetworks.editableprofile.ui.profile.viewModel.EditProfileViewModelFactory
import com.sparknetworks.editableprofile.utli.CitiesListAdapter
import com.sparknetworks.editableprofile.utli.Common
import com.sparknetworks.editableprofile.utli.capitalizeWords
import com.sparknetworks.editableprofile.utli.hideKeyboard
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class EditProfileActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val editProfileViewModelFactory: EditProfileViewModelFactory by instance()
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var profile: Profile
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var awesomeValidation: AwesomeValidation
    private val cityQuerySubject = PublishSubject.create<String>()
    private var citiesList = ArrayList<City?>()
    private val calender: Calendar = Calendar.getInstance()
    private var profileImageUri: Uri? = null
    private val IMAGE_PICKER_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        editProfileViewModel = ViewModelProvider(
            this,
            editProfileViewModelFactory
        ).get(EditProfileViewModel::class.java)
        editProfileViewModel.getCurrentUserProfile()
        handleTheCallingActivity()
        observeSearchForCityEditText()
        searchForCityObserver()
        observeCitySearchResult()
        init()
        observeCurrentUserProfile()
        validateProfileForm()
    }

    private fun handleTheCallingActivity() {
        when (intent.getStringExtra(Common.ACTIVITY_NAME_EXTRA)) {
            Common.REGISTER_FRAGMENT_EXTRA -> {
                text_input_height.visibility = View.VISIBLE
                supportActionBar?.hide()
                AlertDialog.Builder(this)
                    .setTitle("Register Successfully")
                    .setMessage("Congratulations, You have registered successfully.\nNow please complete your profile.")
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            Common.PROFILE_ACTIVITY_EXTRA -> {
                text_input_height.visibility = View.GONE
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getFixedValues()
    }

    private fun init() {
        profile = Profile()
        profile_save_btn.isEnabled = false
        datePickerDialog = DatePickerDialog(
            this, birthdayDatePicker(), calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        profile_edit_profile_image.setOnClickListener {
            Intent(
                Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
            ).also {
                it.type = "image/*"
                startActivityForResult(it, IMAGE_PICKER_REQUEST_CODE)
            }
        }
        profile_save_btn.setOnClickListener {
            hideKeyboard()
            if (!awesomeValidation.validate()) return@setOnClickListener
            buildUpdateProfileRequest()
            editProfileViewModel.updateProfile(profile = profile, profileImageUri = profileImageUri)
        }
        profile_birthday.setOnClickListener { datePickerDialog.show() }

        editProfileViewModel.getOnUpdateSuccess().observe(this, Observer {
            Intent(this, ProfileActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(it)
            }

            finish()
        })
        editProfileViewModel.getOnUpdateError().observe(this, Observer {
            Toast.makeText(this, getString(R.string.unexpected_error), Toast.LENGTH_LONG).show()
        })
        editProfileViewModel.getOnLoading().observe(this, Observer {
            profile_loading.visibility = it
        })

    }

    private fun validateProfileForm() {
        awesomeValidation = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        awesomeValidation.addValidation(this, R.id.text_input_display_name, ".{1,}", R.string.required)
        awesomeValidation.addValidation(this, R.id.text_input_real_name, ".{1,}", R.string.required)
        awesomeValidation.addValidation(this, R.id.text_input_birthday, ".{1,}", R.string.required)
        awesomeValidation.addValidation(this, R.id.text_input_gender, ".{1,}", R.string.required)
        awesomeValidation.addValidation(this, R.id.text_input_marital_status, ".{1,}", R.string.required)
        awesomeValidation.addValidation(this, R.id.text_input_location, ".{1,}", R.string.required)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeSearchForCityEditText() {
        profile_location.doOnTextChanged { text, _, _, _ ->
            cityQuerySubject.onNext(text.toString().capitalizeWords())
        }
    }

    private fun getFixedValues() {
        editProfileViewModel.getFixedValues()
        editProfileViewModel.getFixedValuesLiveData().observe(this, Observer {
            val genderAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, it.genderArray)
            profile_gender.setAdapter(genderAdapter)

            val ethnicityAdapter =
                ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, it.ethnicityList)
            profile_ethnicity.setAdapter(ethnicityAdapter)

            val religionsAdapter =
                ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, it.religionList)
            profile_religion.setAdapter(religionsAdapter)

            val figureAdapter =
                ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, it.figureList)
            profile_figure.setAdapter(figureAdapter)

            val maritalStatusAdapter =
                ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, it.maritalStatusList)
            profile_marital_status.setAdapter(maritalStatusAdapter)
        })
    }

    @SuppressLint("CheckResult")
    private fun searchForCityObserver() {
        cityQuerySubject
            .debounce(400, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap { return@switchMap Observable.just(it) }
            .subscribeOn(Schedulers.io())
            .subscribe { if (it.isNotEmpty()) editProfileViewModel.searchForCity(it) }
    }

    private fun observeCitySearchResult() {
        val adapter = CitiesListAdapter(this, android.R.layout.simple_dropdown_item_1line)
        profile_location.setAdapter(adapter)
        editProfileViewModel.getSearchCitiesResultLiveData().observe(this, Observer {
            profile_save_btn.isEnabled = true
            citiesList.addAll(it)
            val cities = it.map { cities ->
                cities?.city ?: ""
            }
            adapter.setData(cities)
        })
    }

    private fun buildUpdateProfileRequest() {
        profile.displayName = profile_display_name.text.toString()
        profile.realName = profile_real_name.text.toString()
        profile.gender = profile_gender.text.toString()
        profile.location = citiesList.filter { it?.city == profile_location.text.toString() }[0]
        profile.birthday = profile_birthday.text.toString()
        profile.ethnicity = profile_ethnicity.text.toString()
        profile.religion = profile_religion.text.toString()
        profile.figure = profile_figure.text.toString()
        profile.height = profile_height.text.toString().toInt()
        profile.occupation = profile_occupation.text.toString()
        profile.maritalStatus = profile_marital_status.text.toString()
        profile.aboutMe = profile_about.text.toString()
    }

    private fun observeCurrentUserProfile() {
        editProfileViewModel.getUserProfileLiveData().observe(this, Observer {
            profile_display_name.setText(it.displayName)
            profile_real_name.setText(it.realName)
            profile_birthday.setText(it.birthday)
            profile_gender.setText(it.gender)
            profile_ethnicity.setText(it.ethnicity)
            profile_religion.setText(it.religion)
            profile_height.setText(it.height.toString())
            profile_figure.setText(it.figure)
            profile_occupation.setText(it.occupation)
            profile_marital_status.setText(it.maritalStatus)
            profile_location.setText(it.location?.city)
            profile_about.setText(it.aboutMe)
            if (it.profilePicture.isNotEmpty()) {
                Picasso.get().load(it.profilePicture)
                    .into(profile_image)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun birthdayDatePicker(): DatePickerDialog.OnDateSetListener? {
        return DatePickerDialog.OnDateSetListener { _, year, month, day ->
            profile_birthday.setText("$day.${month + 1}.$year")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            profileImageUri = data?.data
            profile_image.setImageURI(profileImageUri)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editProfileViewModel.clearDisposable()
    }

}
