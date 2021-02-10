package org.tridzen.mamafua.ui.home.launcher.apply

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_apply.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Job
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.REQUEST_SENT
import org.tridzen.mamafua.ui.home.HomeViewModel
import org.tridzen.mamafua.ui.home.OnUserIdFound
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.showSnackBar
import javax.inject.Inject

@AndroidEntryPoint
class ApplyFragment : Fragment(R.layout.fragment_apply), OnUserIdFound {

    private val viewModel by viewModels<HomeViewModel>()
    private val jobViewModel by viewModels<JobViewModel>()

    @Inject lateinit var appPreferences: AppPreferences
    private var selectedImageUri: Uri? = null
    private var read: Boolean? = false
    private var onUserIdFound: OnUserIdFound = this

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivId.setOnClickListener {
            openImageChooser()
        }

        butSubmit.setOnClickListener {
            if (read == true) uploadImage()
            else view.showSnackBar("You need to agree to the terms and conditions first")
        }

        cbTerms.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) read = true
        }

        appPreferences.getValue(REQUEST_SENT).asLiveData().observe(viewLifecycleOwner) {
            it?.let {
                cdBanner.visibility = if (it) View.VISIBLE else View.GONE
                tvRequest.visibility = if (it) View.GONE else View.VISIBLE
                clForm.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        Coroutines.main {
            viewModel.getLoggedInUser.await().observe(viewLifecycleOwner) {
                val show = it.requesting
                cdBanner.visibility = if (show) View.VISIBLE else View.GONE
                tvRequest.visibility = if (show) View.GONE else View.VISIBLE
                clForm.visibility = if (show) View.GONE else View.VISIBLE
            }
        }
    }

    private fun uploadImage() {
        Coroutines.main {
            viewModel.getLoggedInUser.await().observe(viewLifecycleOwner) {
                onUserIdFound.userIdFound(it._id)
            }
        }
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    ivId.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun getValues(): Job {
        val firstName = etName.text.toString().trim()
        val lastName = etSecond.text.toString().trim()
        val number = etNumber.text.toString().trim()
        val location = spAreas.selectedItem.toString()

        if (firstName.isEmpty() or firstName.isBlank()) {
            view?.showSnackBar("This field is required")
        }

        if (lastName.isEmpty() or lastName.isBlank()) {
            view?.showSnackBar("This field is required")
        }

        if (number.isEmpty() or number.isBlank()) {
            view?.showSnackBar("This field is required")
        }

        if (number.length < 10) {
            view?.showSnackBar("Invalid number entered?")
        }
        return createJob(firstName, lastName, number, location)
    }

    private fun createJob(
        firstName: String,
        lastName: String,
        phone: String,
        location: String
    ): Job = Job(firstName, lastName, phone, location)

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }

    override fun userIdFound(id: String) {
        Log.d("UserId", "userIdFound: $id")
        jobViewModel.sendApplication(
            getValues(), id
        )
    }
}