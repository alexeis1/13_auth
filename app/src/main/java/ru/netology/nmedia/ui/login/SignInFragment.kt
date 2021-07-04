package ru.netology.nmedia.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
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
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        val testLoginReady = object : TextWatcher {
            override fun afterTextChanged (s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged    (s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.login.isEnabled = binding.username.text.isNotBlank() &&
                                          binding.password.text.isNotBlank()
            }
        }

        binding.username.addTextChangedListener(testLoginReady)
        binding.password.addTextChangedListener(testLoginReady)

        binding.login.setOnClickListener {
            viewModel.login(
                binding.username.text.toString(),
                binding.password.text.toString())
        }

        viewModel.loginState.observe(viewLifecycleOwner){
            binding.loading.visibility = if (!it.isLogin) View.VISIBLE else View.GONE
            if (it.error){
                Snackbar.make(
                    binding.root,
                    "Login Error",
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (it.isLogin){
                findNavController().navigateUp()
            }
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signOutFragment)
        }

        return binding.root
    }
}

