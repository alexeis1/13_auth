package ru.netology.nmedia.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentSignOutBinding

class SignOutFragment : Fragment() {
    private val viewModel: SignOutViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onPrepareOptionsMenu(menu: Menu){
        super.onPrepareOptionsMenu(menu)
        menu.setGroupVisible(R.id.unauthenticated, false)
        menu.setGroupVisible(R.id.authenticated,   false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val binding = FragmentSignOutBinding.inflate(inflater, container, false)

        binding.confirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.confirmPassword.text.isNotBlank() &&
                binding.confirmPassword.text.toString() != binding.password.text.toString()
            ) {
                Snackbar.make(binding.root, getString(R.string.pass_not_match),
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        val testRegisterReady = object : TextWatcher {
            override fun afterTextChanged (s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged    (s: CharSequence?, start: Int, before: Int, count: Int) {
                var res = binding.name.text.isNotBlank() &&
                        binding.username.text.isNotBlank() &&
                        binding.password.text.isNotBlank() &&
                        binding.confirmPassword.text.isNotBlank()
                    res = res && binding.confirmPassword.text.toString() ==
                                 binding.password.text.toString()
                binding.register.isEnabled = res
            }
        }

        binding.name.addTextChangedListener(testRegisterReady)
        binding.username.addTextChangedListener(testRegisterReady)
        binding.password.addTextChangedListener(testRegisterReady)
        binding.confirmPassword.addTextChangedListener(testRegisterReady)

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        viewModel.changePhoto(it.data?.data)
                        Glide.with(binding.imageView)
                            .load(it.data?.data).placeholder(R.drawable.ic_baseline_add_a_photo_48)
                            .centerCrop().into(binding.imageView)
                    }
                }
            }

        binding.imageView.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.register.setOnClickListener {
            viewModel.register(
                binding.name.text.toString(),
                binding.username.text.toString(),
                binding.password.text.toString())
        }

        viewModel.registerState.observe(viewLifecycleOwner){
            binding.loading.visibility = if (it.isRegistering) View.VISIBLE else View.GONE
            if (it.error){
                Snackbar.make(
                    binding.root,
                    getString(R.string.register_Error),
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (!it.isRegistering){
                findNavController().navigateUp()
            }

        }

        return binding.root
    }
}

