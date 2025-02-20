package com.messenger.messenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.messenger.messenger.databinding.FragmentAuthBinding

class AuthPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    // Указываем количество элементов, которые будут отображаться в ViewPager
    override fun getItemCount(): Int = 2

    // Возвращаем фрагменты для каждой позиции
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}

class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    // Добавляем переменную для TabLayoutMediator
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

        // Привязываем созданный ранее адаптер к ViewPager
        binding.viewPager.adapter = AuthPagerAdapter(this)

        // Создаем TabLayoutMediator
        tabsMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Возвращаем желаемое название для каждой позиции
            when (position) {
                0 -> tab.text = "Войти"
                1 -> tab.text = "Регистрация"
            }
        }

        // Привязываем созданный TabLayoutMediator к фрагменту
        tabsMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Отвязываем TabLayoutMediator при уничтожении фрагмента
        tabsMediator.detach()

        // Очищаем binding
        _binding = null
    }
}