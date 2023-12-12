package com.aelzohry.topsaleqatar.ui.comments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentCommentsBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.showProgress
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.Comment
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.CommentsResponse
import com.aelzohry.topsaleqatar.ui.MainActivity
import com.aelzohry.topsaleqatar.ui.ad_details.RecentCommentsAdapter
import com.aelzohry.topsaleqatar.ui.messages.ChatFragment
import com.aelzohry.topsaleqatar.ui.user.UserFragment
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import retrofit2.Call

class CommentsFragment : BaseActivity<FragmentCommentsBinding, CommentViewModel>(), RecentCommentsAdapter.CommentActionsListener {

    private var ad: Ad? = null

    private lateinit var repository: Repository
    private lateinit var adapter: RecentCommentsAdapter
    private var commentsCall: Call<CommentsResponse>? = null

    override fun getLayoutID(): Int = R.layout.fragment_comments

    override fun getViewModel(): CommentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]

    override fun setupUI() {

        initToolbar(getString(R.string.comments))
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

        adapter = RecentCommentsAdapter(ad!!, arrayListOf(), this)
        binding.recyclerView.adapter = adapter
        refresh()
    }

    override fun onClickedListener() {

    }

    override fun observerLiveData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            ad = it.getParcelableExtra(ARG_AD)
        }
        repository = RemoteRepository()
    }

    override fun onDestroy() {
        commentsCall?.cancel()
        super.onDestroy()
    }

    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var didLoadAllPages: Boolean = false

    private fun refresh() {
        commentsCall?.cancel()
        adapter.comments.clear()
        adapter.notifyDataSetChanged()
        didLoadAllPages = false
        isLoading = false
        currentPage = 1
        loadData(1)
    }

    private fun loadData(page: Int) {
        if (isLoading || didLoadAllPages) return
        startLoading()
        commentsCall = repository.getComments(ad!!._id, page) { response ->
            response?.data?.comments?.let {
                if (it.isEmpty()) {
                    didLoadAllPages = true
                } else {
                    this.adapter.comments.addAll(it)
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
        adapter.notifyDataSetChanged()
        checkEmptyData()
    }

    private fun checkEmptyData() {
        binding.noDataTextView.visibility = if (adapter.comments.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun delete(comment: Comment) {
        val context = this ?: return
        val progress = showProgress(context)
        repository.deleteComment(comment._id) { success, message ->
            progress.dismiss()
            Helper.showToast(this, message)
            if (success) {
                this.adapter.comments.remove(comment)
                this.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun edit(comment: Comment) {
        val dialog = AlertDialog.Builder(this)
        val editText = EditText(this)
        editText.hint = getString(R.string.comment)
        editText.setText(comment.text)
        dialog.setTitle(getString(R.string.edit_comment))
        dialog.setView(editText)

        dialog.setPositiveButton(getString(R.string.edit)) { _, _ ->
            val text = editText.text.toString()
            if (text.isEmpty()) return@setPositiveButton
            updateComment(comment, text)
        }

        dialog.setNegativeButton(getString(R.string.cancel)) { _, _ ->

        }

        dialog.show()
    }

    private fun updateComment(comment: Comment, text: String) {
        val context = this ?: return
        val progress = showProgress(context)
        repository.editComment(comment._id, text) { response ->
            val message = response?.message ?: "Error"
            val success = response?.success ?: false
            val updatedComment = response?.data?.comment

            progress.dismiss()
            Helper.showToast(this, message)

            if (success) {
                if (updatedComment != null) {
                    comment.text = updatedComment.text
                    adapter.notifyDataSetChanged()
                } else {
                    this.refresh()
                }
            }
        }
    }

    override fun block(comment: Comment) {
        if (!vm.isLogin) {
            vm.snackLogin.postValue(true)
            return
        }

        val ad = this.ad ?: return
        val adUser = ad.user ?: return
        val user = comment.user ?: return
        val isAuthor = user._id == adUser._id

        val context = this ?: return
        val progress = showProgress(context)
        repository.blockUser(user._id) { success, message ->
            Helper.showToast(this, message)
            progress.dismiss()
            if (success) {
                if (isAuthor) {
                    startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                } else {
                    this.refresh()
                }
            }
        }
    }

    override fun profile(comment: Comment) {
        val user = comment.user ?: return
        UserFragment.newInstance_(this, user, null, user._id)
//        (activity as MainActivity).pushFragment(fragment)
    }

    override fun call(comment: Comment) {
        val user = comment.user ?: return
        Helper.callPhone(user.mobile, this)
    }

    override fun chat(comment: Comment) {
        val context = this ?: return
        val progress = showProgress(context)
        repository.newChannel(null, comment.user?._id) { channel, message ->
            progress.dismiss()
            if (channel == null) {
                Helper.showToast(context, message)
                return@newChannel
            }

            val fragment = ChatFragment.newInstance(this, channel, comment.user?.mobile)
//            (activity as MainActivity).pushFragment(fragment)
        }
    }

    override fun email(comment: Comment) {
        val user = comment.user ?: return
        val email = user.email ?: return
        Helper.sendEmail(email, this)
    }

    companion object {
        private val TAG = "CommentsFragment"
        private const val ARG_AD = "AD"

        @JvmStatic
        fun newInstance(context: Context, ad: Ad) = context.startActivity(Intent(context, CommentsFragment::class.java).putExtra(ARG_AD, ad))
//            CommentsFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable(ARG_AD, ad)
//                }
//            }
    }
}
