package com.utsman.hiyahiyahiya.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.repository.ContactRepository
import com.utsman.hiyahiyahiya.data.repository.StoryRepository
import com.utsman.hiyahiyahiya.model.row.RowStory
import com.utsman.hiyahiyahiya.model.utils.toStory
import com.utsman.hiyahiyahiya.ui.adapter.StoryAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.StoryViewModel
import kotlinx.android.synthetic.main.fragment_stories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StoriesFragment : Fragment() {
    private val storyViewModel: StoryViewModel by inject()
    private val storyAdapter: StoryAdapter by inject()
    private val contactRepository: ContactRepository by inject()
    private val storyRepository: StoryRepository by inject()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout = LinearLayoutManager(context)
        rv_stories.apply {
            layoutManager = linearLayout
            adapter = storyAdapter
        }

        storyViewModel.stories().observe(viewLifecycleOwner, Observer {
            GlobalScope.launch {
                val stories = it.map { l -> l.toStory(contactRepository, storyRepository) }
                CoroutineScope(Dispatchers.Main).launch {
                    if (stories.isEmpty()) {
                        storyAdapter.addStories(listOf(RowStory.Empty("Story is empty")))
                    } else {
                        storyAdapter.addStories(stories)
                    }
                }
            }
            //tx_test_story.text = it.toString()
        })

        storyViewModel.imageBBs().observe(viewLifecycleOwner, Observer {
            //tx_test_story.append("\n --> \n$it")
        })
    }
}