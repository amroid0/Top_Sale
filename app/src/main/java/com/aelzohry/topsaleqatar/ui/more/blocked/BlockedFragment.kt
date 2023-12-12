package com.aelzohry.topsaleqatar.ui.more.blocked

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentBlockedBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.showProgress
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.UsersListResponse
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call

class BlockedFragment : BaseActivity<FragmentBlockedBinding, BaseViewModel>() {

    private lateinit var repository: Repository
    private lateinit var blockedAdapter: BlockedAdapter
    private var usersCall: Call<UsersListResponse>? = null

    override fun getLayoutID(): Int = R.layout.fragment_blocked

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {
        repository = RemoteRepository()

        initToolbar(getString(R.string.blocked_list))
        binding.refreshLayout.setOnRefreshListener {
            refresh()
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                // check for scroll down
                {
                    val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !didLoadAllPages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loadNextPage()
                        }
                    }
                }
            }
        })

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        blockedAdapter = BlockedAdapter(arrayListOf()) {
            unblockUser(it)
        }
        binding.recyclerView.adapter = blockedAdapter
        refresh()

    }

    override fun onClickedListener() {

    }

    override fun observerLiveData() {

    }

    override fun onDestroy() {
        usersCall?.cancel()
        super.onDestroy()
    }

    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var didLoadAllPages: Boolean = false

    private fun refresh() {
        usersCall?.cancel()
        blockedAdapter.users.clear()
        blockedAdapter.notifyDataSetChanged()
        didLoadAllPages = false
        isLoading = false
        currentPage = 1
        loadData(1)
    }

    private fun loadData(page: Int) {
        if (isLoading || didLoadAllPages) return
        startLoading()
        usersCall = repository.getBlockedList(page) { users, messsage ->
            users?.let {
                if (it.isEmpty()) {
                    didLoadAllPages = true
                } else {
                    this.blockedAdapter.users.addAll(it)
                    currentPage = page
                }
            }
            endLoading()
        }
    }

    private fun loadNextPage() {
        loadData(currentPage + 1)
    }

    private fun startLoading() {
        isLoading = true
        binding.refreshLayout.isRefreshing = true
        binding.noDataTextView.visibility = View.GONE
    }

    private fun endLoading() {
        isLoading = false
        binding.refreshLayout.isRefreshing = false
        blockedAdapter.notifyDataSetChanged()
        checkEmptyData()
    }

    private fun checkEmptyData() {
        binding.noDataTextView.visibility = if (blockedAdapter.users.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun unblockUser(user: User) {
        val context = this ?: return
        val progress = showProgress(context)
        repository.unblockUser(user._id) { success, message ->
            progress.dismiss()
            if (success) {
                val position = blockedAdapter.users.indexOf(user)
                if (position >= 0) {
                    blockedAdapter.users.removeAt(position)
                    blockedAdapter.notifyItemRemoved(position)
                    checkEmptyData()
                }
            } else {
                Helper.showToast(context, message)
            }
        }
    }

}
