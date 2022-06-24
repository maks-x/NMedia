package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostContentBinding
import ru.netology.nmedia.utils.LINK_KEY
import ru.netology.nmedia.utils.showKeyboard

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val incomingIntent = intent ?: return

        val postContent = incomingIntent.getStringExtra(
            PostContentActivityResultContract.EDIT_CONTENT_EXTRA_TEXT
        )

        with(binding) {
            if (postContent.isNullOrBlank()) {
                this@PostContentActivity.setTitle(R.string.add_post_title)
            } else {
                edit.setText(postContent)
                edit.hint = postContent
                this@PostContentActivity.setTitle(R.string.edit_post_title)
            }

            edit.requestFocus()
            showKeyboard()

            ok.setOnClickListener {
                val outgoingIntent = Intent()

                if (binding.edit.text.isNullOrBlank()) {
                    setResult(Activity.RESULT_CANCELED, outgoingIntent)
                } else {
                    val content = binding.edit.text.toString()
                    val link = binding.editLink.text.toString()
                    outgoingIntent
                        .putExtra(
                            PostContentActivityResultContract.RESULT_KEY_POST_CONTENT, content
                        ).putExtra(
                            LINK_KEY, link
                        )

                    setResult(Activity.RESULT_OK, outgoingIntent)
                }

                finish()
            }
        }
    }
}

class PostContentActivityResultContract : ActivityResultContract<String, Array<String?>?>() {
    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, PostContentActivity::class.java).apply {
            putExtra(EDIT_CONTENT_EXTRA_TEXT, input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Array<String?>? =
        if (resultCode == AppCompatActivity.RESULT_OK) {
            arrayOf(
                intent?.getStringExtra(RESULT_KEY_POST_CONTENT),
                intent?.getStringExtra(LINK_KEY)
            )
        } else null

    companion object {
        const val EDIT_CONTENT_EXTRA_TEXT = "editPost"
        const val RESULT_KEY_POST_CONTENT = "postContent"
    }
}
