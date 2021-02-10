package org.tridzen.mamafua.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.SignUp
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.databinding.FragmentRegisterBinding
import org.tridzen.mamafua.ui.home.HomeActivity
import org.tridzen.mamafua.ui.home.HomeViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.enable
import org.tridzen.mamafua.utils.handleApiError
import org.tridzen.mamafua.utils.startNewActivity
import org.tridzen.mamafua.utils.visible

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.butRegister.enable(false)
        binding.lavLogin.visible(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, {
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
                    handleApiError(it) { register() }
                    binding.butRegister.alpha = 1f
                    binding.lavLogin.visible(false)
                    binding.butRegister.enable(true)
                }
                is Resource.Loading -> {
                    binding.butRegister.alpha = 0.1f
                    binding.lavLogin.visible(true)
                }
            }
        })

        binding.tetConfirm.addTextChangedListener {
            val email = binding.tetEmail.text.toString().trim()
            val username = binding.tetName.text.toString().trim()
            val password = binding.tetPassword.text.toString().trim()

            binding.butRegister.enable(
                email.isNotEmpty()
                        && it.toString().isNotEmpty()
                        && username.isNotEmpty()
                        && confirmPassword(
                    password, it.toString()
                )
            )
        }

        binding.butRegister.setOnClickListener {
            register()
        }

        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun register() {
        val email = binding.tetEmail.text.toString().trim()
        val password = binding.tetPassword.text.toString().trim()
        val confirm = binding.tetPassword.text.toString().trim()
        val username = binding.tetName.text.toString().trim()

        if (confirmPassword(password, confirm)) {
            val user = SignUp(email = email, password = password, username = username)
            viewModel.register(user)
        }
    }

    private fun confirmPassword(initial: String, second: String): Boolean {
        return initial == second
    }
}