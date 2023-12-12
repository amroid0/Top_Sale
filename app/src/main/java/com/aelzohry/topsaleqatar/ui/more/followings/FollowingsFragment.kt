package com.aelzohry.topsaleqatar.ui.more.followings

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentFollowingsBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.showProgress
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.UsersListResponse
import com.aelzohry.topsaleqatar.ui.more.followers.FollowersAdapter
import com.aelzohry.topsaleqatar.ui.user.UserFragment
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call

class FollowingsFragment : BaseActivity<FragmentFollowingsBinding, BaseViewModel>() {

    private lateinit var repository: Repository
    private lateinit var followersAdapter: FollowersAdapter
    private var followingsCall: Call<UsersListResponse>? = null

    private var userId: String = ""

    companion object {
        fun newInstance(mActivity: Context, userId: String?): Intent {
            val intent = Intent(mActivity, FollowingsFragment::class.java)
            intent.putExtra("user_id", userId)
            return intent
        }
    }

    fun getIntentData() {
        intent?.let {
            userId = it.getStringExtra("user_id").toString()
        }
    }

    override fun getLayoutID(): Int = R.layout.fragment_followings

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {
        getIntentData()
        repository = RemoteRepository()
        initToolbar(getString(R.string.followings))
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
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
//        followingsAdapter = FollowersAdapter(arrayListOf(), {
//            unfollowUser(it)
//        }) {
//            onUserPressed(it)
//        }

        followersAdapter = FollowersAdapter(this, object : FollowersAdapter.OnClickListener {
            override fun onItemClick(item: User) {
            }

            override fun onFollowClick(position: Int, item: User) {
                if (item.isFollowing) {
                    unfollowUser(item, position)
                } else {
                    followUser(item, position)
                }
                followersAdapter.toggleIsFollowed(position)
            }

        }, arrayListOf()) {
            onUserPressed(it)
        }
        binding.recyclerView.adapter = followersAdapter
        refresh()


    }

    private fun unfollowUser(user: User, position: Int) {
        val context = this ?: return
        val progress = showProgress(context)
        repository.unFollowUser(user._id) { success, message ->
            progress.dismiss()
            if (success) {
                setResult(RESULT_OK)
//                val position = followersAdapter.users.indexOf(user)
//                if (position >= 0) {
//                    followersAdapter.users.removeAt(position)
//                    followersAdapter.notifyItemRemoved(position)
//                    checkEmptyData()
//                }
            } else {
                followersAdapter.toggleIsFollowed(position)
            }
            Helper.showToast(context, message)
        }
    }

    private fun followUser(user: User, position: Int) {
        val context = this ?: return
        val progress = showProgress(context)
        repository.followUser(user._id) { success, message ->
            progress.dismiss()
            if (success) {
                setResult(RESULT_OK)
//                val position = followersAdapter.users.indexOf(user)
//                if (position >= 0) {
//                    followersAdapter.users.removeAt(position)
//                    followersAdapter.notifyItemRemoved(position)
//                    checkEmptyData()
//                }
            } else {
                followersAdapter.toggleIsFollowed(position)
            }
            Helper.showToast(context, message)
        }
    }

    override fun onClickedListener() {

    }

    override fun observerLiveData() {

    }

    override fun onDestroy() {
        followingsCall?.cancel()
        super.onDestroy()
    }

    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var didLoadAllPages: Boolean = false

    private fun refresh() {
        followingsCall?.cancel()
        followersAdapter.users.clear()
        followersAdapter.notifyDataSetChanged()
        didLoadAllPages = false
        isLoading = false
        currentPage = 1
        loadData(1, userId)
    }

    private fun loadData(page: Int, userId: String) {
        if (isLoading || didLoadAllPages) return
        startLoading()
        followingsCall = repository.getFollowings(page, userId) { users, messsage ->
            users?.let {
                if (it.isEmpty()) {
                    didLoadAllPages = true
                } else {
                    this.followersAdapter.users.addAll(it)
                    currentPage = page
                }
            }
            endLoading()
        }
    }

    private fun loadNextPage() {
        loadData(currentPage + 1, userId)
    }

    private fun startLoading() {
        isLoading = true
        binding.refreshLayout.isRefreshing = true
        binding.noDataTextView.visibility = View.GONE
    }

    private fun endLoading() {
        isLoading = false
        binding.refreshLayout.isRefreshing = false
        followersAdapter.notifyDataSetChanged()
        checkEmptyData()
    }

    private fun checkEmptyData() {
        binding.noDataTextView.visibility = if (followersAdapter.users.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun unfollowUser(user: User) {
        val context = this ?: return
        val progress = showProgress(context)
        repository.unFollowUser(user._id) { success, message ->
            progress.dismiss()
            if (success) {
                val position = followersAdapter.users.indexOf(user)
                if (position >= 0) {
                    followersAdapter.users.removeAt(position)
                    followersAdapter.notifyItemRemoved(position)
                    checkEmptyData()
                }
            } else {
                Helper.showToast(context, message)
            }
        }
    }

    private fun onUserPressed(user: User) {
        openUserProfile(user)
    }

    private fun openUserProfile(user: User) {
        val intent: Intent = UserFragment.newInstance(this, user, null, user._id)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == RESULT_OK) {
                refresh()
                setResult(RESULT_OK)
            }
        }
    }
}
