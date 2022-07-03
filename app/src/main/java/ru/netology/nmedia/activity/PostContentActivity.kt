package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostContentBinding
import ru.netology.nmedia.utils.POST_CONTENT_TEXT
import ru.netology.nmedia.utils.POST_CONTENT_VIDEO_LINK

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val incomingIntent = intent ?: return

        val postContent = incomingIntent.extras
        postContent ?: return

        with(binding) {
            if (postContent.isEmpty) {
                this@PostContentActivity.setTitle(R.string.add_post_title)

            } else {
                edit.setText(postContent.getString(POST_CONTENT_TEXT))
                editLink.setText(postContent.getString(POST_CONTENT_VIDEO_LINK))

                this@PostContentActivity.setTitle(R.string.edit_post_title)
            }


//            edit.requestFocus()
//            showKeyboard()

            ok.setOnClickListener {
                val outgoingIntent = Intent()

                if (binding.edit.text.isNullOrBlank()) {
                    setResult(Activity.RESULT_CANCELED, outgoingIntent)
                } else {
                    val text = binding.edit.text.toString()
                    val link = binding.editLink.text.toString()

                    outgoingIntent.putExtras(
                        Bundle().apply {
                            putString(POST_CONTENT_TEXT, text)
                            putString(POST_CONTENT_VIDEO_LINK, link)
                        }
                    )

                    setResult(Activity.RESULT_OK, outgoingIntent)
                }

                finish()
            }
        }
    }
}

class PostContentActivityResultContract : ActivityResultContract<Bundle, Bundle?>() {
    override fun createIntent(context: Context, input: Bundle): Intent =
        Intent(context, PostContentActivity::class.java).apply {
            putExtras(input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Bundle? =
        if (resultCode == AppCompatActivity.RESULT_OK) {
            intent?.extras
        } else null
}
