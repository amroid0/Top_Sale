package com.aelzohry.topsaleqatar.ui.carShows.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentCarShowsBinding
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.UsersListResponse
import com.aelzohry.topsaleqatar.ui.carShows.adapter.CarShowsAdapter
import com.aelzohry.topsaleqatar.ui.carShows.viewModel.CarShowsViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet
import retrofit2.Call

class CarShowsFragment : BaseBottomSheet<FragmentCarShowsBinding, CarShowsViewModel>() {

    private lateinit var repository: Repository
    private lateinit var userAdapter: CarShowsAdapter
    private var carShowsCall: Call<UsersListResponse>? = null

    companion object {
        @JvmStatic
        fun newInstance() = CarShowsFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun getLayoutID(): Int = R.layout.fragment_car_shows

    override fun getViewModel(): CarShowsViewModel = ViewModelProvider(this).get(CarShowsViewModel::class.java)

    override fun setupUI() {
        repository = RemoteRepository()
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
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
//        followingsAdapter = FollowersAdapter(arrayListOf(), {
//            unfollowUser(it)
//        }) {
//            onUserPressed(it)
//        }

        userAdapter = CarShowsAdapter(requireContext(), arrayListOf()) {
            if (::mListener.isInitialized) {
                mListener.onItemSelected(it)
                dismiss()
            }
        }
        binding.recyclerView.adapter = userAdapter
        refresh()


    }

    override fun onClickedListener() {
        binding.ibClose.setOnClickListener { dismiss() }
    }

    override fun observerLiveData() {
    }

    override fun isFullHeight(): Boolean = true

    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var didLoadAllPages: Boolean = false

    private fun refresh() {
        carShowsCall?.cancel()
        userAdapter.users.clear()
        userAdapter.notifyDataSetChanged()
        didLoadAllPages = false
        isLoading = false
        currentPage = 1
        loadData(1)
    }

    private fun loadData(page: Int) {
        if (isLoading || didLoadAllPages) return
        startLoading()
        carShowsCall = repository.getCarShows(page) { users, messsage ->
            users?.let {
                if (it.isEmpty()) {
                    didLoadAllPages = true
                } else {
                    this.userAdapter.users.addAll(it)
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
        userAdapter.notifyDataSetChanged()
        checkEmptyData()
    }

    private fun checkEmptyData() {
        binding.noDataTextView.visibility = if (userAdapter.users.isEmpty()) View.VISIBLE else View.GONE
    }

    private lateinit var mListener: Listener

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onItemSelected(user: User)
    }

}