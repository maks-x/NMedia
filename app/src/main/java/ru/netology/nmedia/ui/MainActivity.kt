package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.objects.Post

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) return@let

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) return@let
            intent.removeExtra(Intent.EXTRA_TEXT)

            val direction = FeedFragmentDirections.toPostContentFragment(Post(text = text))
            val navController = findNavController(R.id.fragmentContainer)
            navController.navigate(direction)
        }
    }
}