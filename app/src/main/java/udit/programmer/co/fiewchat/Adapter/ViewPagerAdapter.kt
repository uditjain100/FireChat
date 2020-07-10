package udit.programmer.co.fiewchat.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var fragmentsList = mutableListOf<Fragment>()
    var titlesList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment = fragmentsList[position]

    override fun getCount(): Int = fragmentsList.size

    fun addFragment(fragment: Fragment, title: String) {
        fragmentsList.add(fragment)
        titlesList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titlesList[position]
    }

}