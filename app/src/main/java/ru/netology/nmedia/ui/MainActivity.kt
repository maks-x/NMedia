package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ru.netology.nmedia.R
import ru.netology.nmedia.objects.Post

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            handleIntent(it)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleIntent(it)
        }
    }


    private fun handleIntent(it: Intent) {
        if (it.action != Intent.ACTION_SEND) return

        val text = it.getStringExtra(Intent.EXTRA_TEXT)
        if (text?.isNotBlank() != true) return
        intent.removeExtra(Intent.EXTRA_TEXT)
        val postToSend = Post(text = text)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

        val direction = if (it.identifier == INNER_INTENT_IDENTIFIER) {
            PostFragmentDirections.postFragmentToPostContentFragment(postToSend)
        } else FeedFragmentDirections.toPostContentFragment(postToSend)

        val navController = navHostFragment.navController
        navController.navigate(direction)
    }

    companion object {
        const val INNER_INTENT_IDENTIFIER = "netology"
    }

}