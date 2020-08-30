package com.utsman.hiyahiyahiya.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowContact
import com.utsman.hiyahiyahiya.model.utils.chatRoom
import com.utsman.hiyahiyahiya.ui.activity.ChatRoomActivity
import com.utsman.hiyahiyahiya.ui.adapter.ContactAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.AuthViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.ContactViewModel
import com.utsman.hiyahiyahiya.utils.Broadcast
import com.utsman.hiyahiyahiya.utils.generateIdRoom
import com.utsman.hiyahiyahiya.utils.intentTo
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.coroutines.GlobalScope
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactFragment : Fragment() {
    private val contactViewModel: ContactViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private val contactAdapter: ContactAdapter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        rv_contact.run {
            layoutManager = linearLayoutManager
            adapter = contactAdapter
        }

        contactAdapter.onClick { contact ->
            Broadcast.with(GlobalScope).post("direct_main_chat")
            authViewModel.requestUser()?.uid?.let { myId ->
                val room = chatRoom {
                    this.id = generateIdRoom(myId, contact.id)
                    this.membersId = listOf(myId, contact.id)
                    this.imageRoom = contact.photoUrl
                    this.titleRoom = contact.name
                }

                intentTo(ChatRoomActivity::class.java) {
                    putExtra("room", room)
                    putExtra("to", contact.id)
                }
                //finish()
            }
        }

        contactViewModel.contacts.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) contactAdapter.addContact(listOf(RowContact.Empty(text = "Contact empty")))
            else contactAdapter.addContact(it)
        })
    }
}