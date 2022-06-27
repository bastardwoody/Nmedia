package ru.netology.nmedia.data

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.util.*

class InMemoryPostRepository : PostRepository {
    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val initialPost = List(100) { index ->
            Post(
                id = index + 1,
                postHeader = "Нетология. Университет интернет-профессий",
                date = Date().toString(),
                isLiked = false,
                likes = 999999,
                shares = 995,
                views = (0..1000000).random()
            )
        }
        data = MutableLiveData(initialPost)
    }

    override fun likeById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) post.copy(isLiked = !post.isLiked)
            else post
        }
        posts.map { post ->
            if (post.isLiked) post.copy(likes = post.likes++)
            else post.copy(likes = post.likes--)
        }
    }

    override fun shareById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) post.copy(shares = post.shares + 1)
            else post
        }
    }
}

fun slicer(count: Int): String {
    return when (count) {
        in 1_000..999_999 ->
            if ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble() * 10 % 10 == 0.0)
                (count / 1_000).toString() + "K"
            else ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "K"
        in 1_000_000..999_999_999 ->
            if ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble() * 10 % 10 == 0.0)
                (count / 1_000_000).toString() + "M"
            else ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "M"
        else -> count.toString()
    }
}