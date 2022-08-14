package com.example.treasurysolutions.presentation.home.servicefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.treasurysolutions.databinding.FragmentServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment : Fragment() {

    private var binding: FragmentServiceBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentServiceBinding.inflate(inflater)
        return binding?.root
    }
}