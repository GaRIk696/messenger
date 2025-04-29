package com.dpp.messenger.auth.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dpp.messenger.auth.ui.adapter.AuthPagerAdapter
import com.dpp.messenger.databinding.FragmentAuthBinding
import com.google.android.material.tabs.TabLayoutMediator

class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabsMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = AuthPagerAdapter(this)

        tabsMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Войти"
                1 -> tab.text = "Регистрация"
            }
        }

        tabsMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        tabsMediator.detach()

        _binding = null
    }
}