package com.example.nesa_drunk.ui.home.pertemuan_13

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ThirteenthTabsAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabCaptureFragment()
            1 -> TabScanFragment()
            else -> TabQRCodeFragment()
        }
    }
}
