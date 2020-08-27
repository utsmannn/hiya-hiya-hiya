package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowContact
import com.utsman.hiyahiyahiya.model.utils.chatRoom
import com.utsman.hiyahiyahiya.ui.adapter.ContactAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.ContactViewModel
import com.utsman.hiyahiyahiya.utils.generateIdRoom
import com.utsman.hiyahiyahiya.utils.intentTo
import com.utsman.hiyahiyahiya.ui.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_contacts.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsActivity : AppCompatActivity() {
    private val contactViewModel: ContactViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private val contactAdapter: ContactAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val linearLayoutManager = LinearLayoutManager(this)
        rv_contact.run {
            layoutManager = linearLayoutManager
            adapter = contactAdapter
        }

        contactAdapter.onClick { contact ->
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
                finish()
            }
        }

        contactViewModel.contacts.observe(this, Observer {
            if (it.isEmpty()) contactAdapter.addContact(listOf(RowContact.Empty(text = "Contact empty")))
            else contactAdapter.addContact(it)
        })
    }
}