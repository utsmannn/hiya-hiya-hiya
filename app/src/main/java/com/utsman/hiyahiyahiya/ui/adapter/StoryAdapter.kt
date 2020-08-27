package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowStory
import com.utsman.hiyahiyahiya.model.row.RowStoryType
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.load
import com.utsman.hiyahiyahiya.utils.story_view.StoryModel
import kotlinx.android.synthetic.main.item_list_empty.view.*
import kotlinx.android.synthetic.main.item_story.view.*

class StoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(storyItem: RowStory.StoryItem) = itemView.run {
            tx_title.text = storyItem.userName
            tx_time.text = storyItem.stringTime()

            val arrayList: ArrayList<StoryModel> = arrayListOf()
            val storyModels = storyItem.imageBbs.map {
                StoryModel(it.urlLarge, storyItem.userName, storyItem.stringTime())
            }
            arrayList.addAll(storyModels)
            story_view.setImageUris(arrayList)

            click {
                story_view.navigateToStoryPlayerPage(context)
            }
        }
    }

    inner class EmptyStoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: RowStory.Empty) = itemView.run {
            tx_empty_message.text = item.text
        }
    }

    inner class StoryDiffCallback(
        private val oldList: List<RowStory>,
        private val newList: List<RowStory>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    private val list: MutableList<RowStory> = mutableListOf()
    private var onStoryClick: ((RowStory.StoryItem) -> Unit)? = null

    fun onStoryClick(onStoryClick: ((RowStory.StoryItem) -> Unit)? = null) {
        this.onStoryClick = onStoryClick
    }

    fun addStories(stories: List<RowStory>) = apply {
        val diffCallback = StoryDiffCallback(list, stories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(stories)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (RowStoryType.values()[viewType]) {
            RowStoryType.ME -> StoryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_story_me, parent, false)
            )
            RowStoryType.OTHER -> StoryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
            )
            else -> EmptyStoryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_list_empty, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int = list[position].rowStoryType.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item  = list[position]) {
            is RowStory.StoryItem -> (holder as StoryViewHolder).bind(item)
            is RowStory.Empty -> (holder as EmptyStoryViewHolder).bind(item)
        }
    }
}