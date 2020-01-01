package com.sparknetworks.editableprofile.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.ui.profile.viewModel.ProfileViewModel
import com.sparknetworks.editableprofile.ui.profile.viewModel.ProfileViewModelFactory
import com.sparknetworks.editableprofile.utli.Common
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class ProfileActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val profileViewModelFactory: ProfileViewModelFactory by instance()
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        profileViewModel = ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel::class.java)
        observeCurrentUserProfile()
        observeOnError()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getCurrentUserProfile()
    }

    @SuppressLint("SetTextI18n")
    private fun observeCurrentUserProfile() {
        profileViewModel.getUserProfileLiveData().observe(this, Observer {
            profile_display_name.text = it.displayName
            profile_birthday.setText(it.birthday)
            profile_gender.setText(it.gender)
            profile_ethnicity.setText(it.ethnicity)
            profile_religion.setText(it.religion)
            profile_height.setText("${it.height} cm")
            profile_figure.setText(it.figure)
            profile_location.setText(it.location?.city)
            profile_about.setText(it.aboutMe)
            if (it.profilePicture.isNotEmpty()) {
                Picasso.get().load(it.profilePicture)
                    .into(profile_image)
            }
        })
    }

    private fun observeOnError() {
        profileViewModel.getOnError().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_edit_profile) {

            Intent(this, EditProfileActivity::class.java).also {
                it.putExtra(
                    Common.ACTIVITY_NAME_EXTRA,
                    Common.PROFILE_ACTIVITY_EXTRA
                )
                startActivity(it)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
