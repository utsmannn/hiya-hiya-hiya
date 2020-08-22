package com.utsman.hiyahiyahiya.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ChatPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val fragments: MutableList<Fragment> = mutableListOf()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    fun addFragment(vararg fragment: Fragment) {
        this.fragments.addAll(fragment)
        notifyDataSetChanged()
    }
}