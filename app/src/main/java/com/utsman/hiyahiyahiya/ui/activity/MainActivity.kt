package com.utsman.hiyahiyahiya.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.firebase.iid.FirebaseInstanceId
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.model.types.TypeCamera
import com.utsman.hiyahiyahiya.model.types.TypeMessage
import com.utsman.hiyahiyahiya.model.utils.localUser
import com.utsman.hiyahiyahiya.model.utils.messageBody
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.ui.adapter.ChatPagerAdapter
import com.utsman.hiyahiyahiya.ui.fragment.CameraFragment
import com.utsman.hiyahiyahiya.ui.fragment.RoomsFragment
import com.utsman.hiyahiyahiya.ui.fragment.StoriesFragment
import com.utsman.hiyahiyahiya.ui.viewmodel.AuthViewModel
import com.utsman.hiyahiyahiya.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()
    private val localUserDb: LocalUserDatabase by inject()
    private val networkMessage: NetworkMessage by network()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerNotification()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    logi("Instance Id failed")
                } else {
                    val token = task.result?.token
                    registerTokenToAnotherDevice(token)
                }
            }

        btn_contacts.click(MainScope()) {
            if (vp_chat.currentItem == 1) {
                intentTo(ContactsActivity::class.java)
            } else {
                intentTo(CameraActivity::class.java) {
                    putExtra("intent_type", TypeCamera.STORY.name)
                }
            }
        }

        startPermission {
            setupToolbar()
            setupFragment()
        }
    }

    private fun setupToolbar() {
        toolbar_main.inflateMenu(R.menu.main_menu)
        val logoutMenu = toolbar_main.menu.findItem(R.id.action_logout)
        logoutMenu.setOnMenuItemClickListener {
            toast("logout")
            true
        }
    }

    private fun setupFragment() {
        val pagerAdapter = ChatPagerAdapter(supportFragmentManager)
        val cameraFragment = CameraFragment.instance(null, null, TypeCamera.STORY.name)
        val chatFragment = RoomsFragment()
        val storiesFloat = StoriesFragment()
        pagerAdapter.addFragment(cameraFragment, chatFragment, storiesFloat)
        pagerAdapter.addTitles("Camera", "Chat", "Story")
        cameraFragment.isFromPager(true)

        vp_chat.adapter = pagerAdapter
        vp_chat.currentItem = 1
        tab_bar.setupWithViewPager(vp_chat)

        vp_chat.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        btn_contacts.visibility = View.GONE
                        app_bar.setExpanded(false)
                    }
                    1 -> {
                        app_bar.setExpanded(true)
                        btn_contacts.visibility = View.VISIBLE
                        btn_contacts.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_round_chat_24))
                    }
                    2 -> {
                        btn_contacts.visibility = View.VISIBLE
                        btn_contacts.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_round_photo_camera_24))
                    }
                }
            }
        })

        Broadcast.with(GlobalScope).observer { key, _ ->
            when (key) {
                "direct_main_chat" -> runOnUiThread {
                    vp_chat.currentItem = 1
                }
            }
        }
    }

    private fun registerNotification() {

    }

    private fun registerTokenToAnotherDevice(tokenResult: String?) {
        authViewModel.requestUser()?.run {
            val profileAbout = "This about profile of $displayName"
            UserPref.run {
                saveUserId(uid)
                saveUsername(displayName)
            }

            val localUser = localUser {
                id = uid
                name = displayName
                photoUri = photoUrl.toString()
                about = profileAbout
                token = tokenResult
            }

            GlobalScope.launch {
                localUserDb.localUserDao().insert(localUser)
            }

            val messageBody = messageBody {
                fromMessage = localUser.id
                typeMessage = TypeMessage.DEVICE_REGISTER
                payload = localUser
            }

            networkMessage.send(this@MainActivity, messageBody, object : NetworkMessage.MessageCallback {
                override fun onSuccess() {
                    //toast("message sending")
                }

                override fun onFailed(message: String?) {
                    toast("failed -> $message")
                }
            })
        }
    }
}