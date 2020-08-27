package com.utsman.hiyahiyahiya.model.row

import android.annotation.SuppressLint
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.model.features.ImageBBSimple
import java.text.SimpleDateFormat

enum class RowStoryType {
    ME, OTHER, EMPTY
}

sealed class RowStory(val rowStoryType: RowStoryType) {
    data class StoryItem(
        var id: String = "miyabi",
        var userName: String = "",
        var time: Long = 0L,
        var imageBbs: List<ImageBBSimple> = emptyList()
    ) : RowStory(generateTypeStory(userName)) {

        @SuppressLint("SimpleDateFormat")
        fun stringTime(): String {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(time)
        }
    }

    data class Empty(
        var text: String = "Empty"
    ) : RowStory(RowStoryType.EMPTY)

}

fun generateTypeStory(userName: String): RowStoryType {
    return when (userName) {
        UserPref.getUsername() -> RowStoryType.ME
        else -> RowStoryType.OTHER
    }
}