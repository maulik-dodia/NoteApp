package com.noteapp.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.noteapp.NameApplication
import com.noteapp.R
import com.noteapp.databinding.ActivityMainBinding
import com.noteapp.model.Name
import com.noteapp.ui.adapter.NameRVAdapter
import com.noteapp.viewmodel.NameViewModel
import com.noteapp.viewmodel.NameViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding

    private val nameViewModel: NameViewModel by viewModels {
        NameViewModelFactory((application as NameApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val nameAdapter = NameRVAdapter()
        dataBinding.apply {
            viewModel = nameViewModel
            btnSubmit.setOnClickListener {
                nameViewModel.apply {
                    validate()
                    validationValue.observe(this@MainActivity) {
                        if (it) {
                            val name = Name(
                                firstName = this.firstName.value,
                                lastName = this.lastName.value
                            )
                            insertName(name)
                            Log.e("mk", "here...")
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Please enter first and last names!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            rvNames.apply {
                adapter = nameAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
        nameViewModel.allNames.observe(this) { names ->
            names?.let { nameAdapter.submitList(it) }
        }
    }
}