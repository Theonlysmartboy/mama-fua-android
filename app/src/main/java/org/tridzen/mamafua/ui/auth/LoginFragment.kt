package org.tridzen.mamafua.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Login
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.databinding.FragmentLoginBinding
import org.tridzen.mamafua.ui.home.HomeActivity
import org.tridzen.mamafua.ui.home.HomeViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.enable
import org.tridzen.mamafua.utils.handleApiError
import org.tridzen.mamafua.utils.startNewActivity
import org.tridzen.mamafua.utils.visible

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.lavLogin.visible(false)
        binding.butLogin.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, {
            binding.lavLogin.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    Coroutines.main {
                        viewModel.saveAuthToken(it.value.token)
                        viewModel.saveUser(it.value.user)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                        requireActivity().finish()
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { login() }
                    binding.butLogin.alpha = 1f
                    binding.lavLogin.visible(false)
                    binding.butLogin.enable(true)
                }
                is Resource.Loading -> {
                    binding.butLogin.alpha = 0.1f
                }
            }
        })

        binding.tetPassword.addTextChangedListener {
            val email = binding.tetEmail.text.toString().trim()
            binding.butLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.butLogin.setOnClickListener {
            login()
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun login() {
        val email = binding.tetEmail.text.toString().trim()
        val password = binding.tetPassword.text.toString().trim()
        val user = Login(email = email, password = password)
        viewModel.login(user)
    }
}