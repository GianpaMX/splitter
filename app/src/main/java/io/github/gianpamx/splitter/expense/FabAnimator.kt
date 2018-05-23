package io.github.gianpamx.splitter.expense

import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout

class FabAnimator(private val floatingActionButton: FloatingActionButton) : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {
        hideShowButton()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        hideShowButton()
    }

    private fun hideShowButton() {
        floatingActionButton.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton?) {
                floatingActionButton.show()
            }
        })
    }
}
