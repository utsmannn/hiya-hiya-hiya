package com.utsman.hiyahiyahiya.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.ui.ChatRoomActivity
import com.utsman.hiyahiyahiya.ui.adapter.RoomAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.RoomViewModel
import com.utsman.hiyahiyahiya.utils.intentTo
import kotlinx.android.synthetic.main.fragment_rooms.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoomsFragment : Fragment() {
    private val roomViewModel: RoomViewModel by viewModel()
    private val roomAdapter: RoomAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        rv_room.run {
            layoutManager = linearLayoutManager
            adapter = roomAdapter
        }

        roomAdapter.onClick { room ->
            val toMember = room.membersId.first { it != UserPref.getUserId() }
            intentTo(ChatRoomActivity::class.java) {
                putExtra("room", room)
                putExtra("to", toMember)
            }
        }

        roomViewModel.room.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) roomAdapter.addChat(listOf(RowRoom.Empty("Chat not found")))
            else roomAdapter.addChat(it)
        })
    }
}
