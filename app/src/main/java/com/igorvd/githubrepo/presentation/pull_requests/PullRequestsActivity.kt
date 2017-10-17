package com.igorvd.githubrepo.presentation.pull_requests

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.igorvd.githubrepo.R
import com.igorvd.githubrepo.data.PullRequest
import com.igorvd.githubrepo.presentation.EXTRA_OWNER_LOGIN
import com.igorvd.githubrepo.presentation.EXTRA_REPO_NAME
import com.igorvd.githubrepo.utils.EndlessRecyclerViewScrollListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_pull_requests.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class PullRequestsActivity : AppCompatActivity(), PullRequestsContract.View {

    private val LIST_STATE_KEY = "recycler_state"
    private val ITEMS_STATE_KEY = "items_state"
    private var listState : Parcelable? = null

    @Inject
    lateinit var mPresenter : PullRequestsContract.Presenter

    //view objects
    private val mProgressBar : ProgressBar by lazy { pullRequestsPb }
    private val mToolbar : Toolbar by lazy { toolbar }
    private val mCtlToolbar : CollapsingToolbarLayout by lazy { ctlToolbar }


    //recycler view objects
    private val mRecyclerView : RecyclerView by lazy { setupRecyclerView() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this) }
    private val mAdapter : PullRequestsAdapter by lazy {
        PullRequestsAdapter(
                this,
                mVisibleItems,
                onItemClicked = { pullRequest -> onPullRequestClicked(pullRequest) },
                onRetryClick = { loadOpenRepositories() })
    }
    private val mScrollListener : EndlessRecyclerViewScrollListener by lazy { createScrollListener() }
    private val mVisibleItems: ArrayList<PullRequest> by lazy { ArrayList<PullRequest>() }
    private val mOpenItems: ArrayList<PullRequest> by lazy { ArrayList<PullRequest>() }
    private val mClosedItems: ArrayList<PullRequest> by lazy { ArrayList<PullRequest>() }

    //intent received objects
    private lateinit var ownerLogin: String
    private lateinit var repoName: String

    private var blockLoadOpen = false

    private var mLoadRepositoriesJob : Job? = null

    //**************************************************************************
    // ACTIVITY LIFE CYCLE
    //**************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_requests)

        ownerLogin = intent.getStringExtra(EXTRA_OWNER_LOGIN)
        repoName = intent.getStringExtra(EXTRA_REPO_NAME)

        initViews()

        println("On create");

        if(savedInstanceState != null) {

            restoreInstance(savedInstanceState)

        }

        /**
         * we only start the presenter interaction in the onCreate lifecycle if the items weren't
         * saved by the [onSaveInstanceState] method
         */
        if(mVisibleItems.size <= 0) {
            loadOpenRepositories()
        }
    }

    override fun onResume() {

        super.onResume()

        if(listState != null) {
            mLayoutManager.onRestoreInstanceState(listState)
        }
    }

    override fun onDestroy() {

        if(mLoadRepositoriesJob != null && mLoadRepositoriesJob!!.isActive) {
            mLoadRepositoriesJob!!.cancel()
        }

        mPresenter.detachView()

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        listState = mLayoutManager.onSaveInstanceState()
        outState?.putParcelable(LIST_STATE_KEY, listState)
        outState?.putParcelableArrayList(ITEMS_STATE_KEY, mVisibleItems)

    }

    //**************************************************************************
    // VIEW INTERFACE
    //**************************************************************************

    override fun showOpenPullRequests(pullRequests: List<PullRequest>) {

        if(mAdapter.hasFooter) {
            mAdapter.removeFooter()
        }

        mVisibleItems.addAll(pullRequests)
        mAdapter.notifyDataSetChanged()
    }

    override fun blockRetrieveOpen() {
        blockLoadOpen = true
        mAdapter.removeFooter()
    }

    override fun showClosedPullRequests(pullRequests: List<PullRequest>) {

        if(mAdapter.hasFooter) {
            mAdapter.removeFooter()
        }

        mVisibleItems.addAll(pullRequests)
        mAdapter.notifyDataSetChanged()
    }

    override fun clearVisibleList() {
        mVisibleItems.clear()
        mAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {

        if(mVisibleItems.size > 0) {
            mAdapter.showFooterProgress()
        } else {

            mProgressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {

        mProgressBar.visibility = View.GONE

    }

    override fun showError() {

        if(mVisibleItems.size > 0) {
            mAdapter.showFooterError()
        }

    }

    //**************************************************************************
    // INNER METHODS
    //**************************************************************************

    fun loadOpenRepositories() {

        //we don't call the presenter if is already doing something or if it's blocked
        if(blockLoadOpen ||
                (mLoadRepositoriesJob != null && mLoadRepositoriesJob!!.isActive)) {
            return
        }

        mLoadRepositoriesJob = launch(UI) {
            mPresenter.loadOpenPullRequests(ownerLogin, repoName, mVisibleItems.size)
        }
    }

    fun onPullRequestClicked(pullRequest: PullRequest) {
        Toast.makeText(this, "item clicked ${pullRequest.title}", Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {

        /* because we're using the lazy init, we need to use our recyclerView for the objects
      initialize, otherwise we could simple put this method into the [setupRecyclerView()] */
        mRecyclerView.addOnScrollListener(mScrollListener)

        setSupportActionBar(mToolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mCtlToolbar.title = getString(R.string.title_pull_requests)

    }

    private fun restoreInstance(savedInstanceState: Bundle) {

        listState = savedInstanceState.getParcelable(LIST_STATE_KEY)

        val items = savedInstanceState.getParcelableArrayList<PullRequest>(ITEMS_STATE_KEY)

        if(items != null) {
            mVisibleItems.clear()
            mVisibleItems.addAll(items)
        }

    }

    private fun setupRecyclerView() : RecyclerView {

        pullRequestsRv.layoutManager = mLayoutManager
        mAdapter.setHasStableIds(true)
        pullRequestsRv.adapter = mAdapter
        pullRequestsRv.itemAnimator = DefaultItemAnimator()

        return pullRequestsRv
    }

    private fun createScrollListener() : EndlessRecyclerViewScrollListener {

        return object : EndlessRecyclerViewScrollListener(mLayoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                loadOpenRepositories()

            }
        }
    }
}