package com.example.chucknorrisjokes.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chucknorrisjokes.core.ProvideViewModel
import com.example.chucknorrisjokes.core.Representative
import com.example.chucknorrisjokes.core.UiObserver
import com.example.chucknorrisjokes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModel(MainViewModel::class.java)

        binding.jokeButton.setOnClickListener {
            viewModel.loadJoke()
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : UiObserver<UiState>{
            override fun update(data: UiState) {
                data.show(binding)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun <T: Representative<*>> viewModel(clazz: Class<out T>): T {
        return (application as ProvideViewModel).viewModel(clazz)
    }
}
