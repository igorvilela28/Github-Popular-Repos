package com.igorvd.githubrepo.presentation.pull_requests

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.igorvd.githubrepo.R
import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.presentation.EXTRA_OWNER_LOGIN
import com.igorvd.githubrepo.presentation.EXTRA_REPO_NAME
import com.igorvd.githubrepo.utils.EndlessRecyclerViewScrollListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_pull_requests.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.default_error.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class PullRequestsActivity : AppCompatActivity(), PullRequestsContract.View {

    private val LIST_STATE_KEY = "recycler_state"
    private val ITEMS_STATE_KEY = "items_state"
    private var mListState: Parcelable? = null

    @Inject
    protected lateinit var mPresenter : PullRequestsContract.Presenter

    //view objects
    private val mProgressBar : ProgressBar by lazy { progressBar }
    private val mTvError : TextView by lazy { tvError }
    private val mBtnTryAgain : Button by lazy { btnTryAgain }
    private val mToolbar : Toolbar by lazy { toolbar }
    private val mCtlToolbar : CollapsingToolbarLayout by lazy { ctlToolbar }


    //recycler view objects
    private val mRecyclerView : RecyclerView by lazy { setupRecyclerView() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this) }
    private val mAdapter : PullRequestsAdapter by lazy {
        PullRequestsAdapter(
                this,
                mItems,
                onItemClicked = { pullRequest -> onPullRequestClicked(pullRequest) },
                onRetryClick = { loadOpenRepositories() })
    }
    private val mScrollListener : EndlessRecyclerViewScrollListener by lazy { createScrollListener() }

    //items
    private val mItems: ArrayList<PullRequest> by lazy { ArrayList<PullRequest>() }

    //intent received objects
    private lateinit var mOwnerLogin: String
    private lateinit var mRepoName: String

    private var mLoadPullRequestsJob: Job? = null

    //**************************************************************************
    // ACTIVITY LIFE CYCLE
    //**************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_requests)

        mOwnerLogin = intent.getStringExtra(EXTRA_OWNER_LOGIN)
        mRepoName = intent.getStringExtra(EXTRA_REPO_NAME)

        initViews()

        if(savedInstanceState != null) {

            restoreInstance(savedInstanceState)

        }

        /**
         * we only start the presenter interaction in the onCreate lifecycle if the items weren't
         * saved by the [onSaveInstanceState] method
         */
        if(mItems.size <= 0) {
            loadOpenRepositories()
        }
    }

    override fun onResume() {

        super.onResume()

        if(mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState)
        }
    }

    override fun onDestroy() {

        if(mLoadPullRequestsJob != null && mLoadPullRequestsJob!!.isActive) {
            mLoadPullRequestsJob!!.cancel()
        }

        mPresenter.detachView()

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        mListState = mLayoutManager.onSaveInstanceState()
        outState?.putParcelable(LIST_STATE_KEY, mListState)
        outState?.putParcelableArrayList(ITEMS_STATE_KEY, mItems)

    }

    //**************************************************************************
    // VIEW INTERFACE
    //**************************************************************************

    override fun showOpenPullRequests(pullRequests: List<PullRequest>) {

        if(mAdapter.hasFooter) {
            mAdapter.removeFooter()
        }

        mItems.addAll(pullRequests)
        mAdapter.notifyDataSetChanged()
    }

    override fun onOpenEmpty() {

        if(mItems.isEmpty()) {
            mTvError.setText(R.string.empty_content)
            mTvError.visibility = View.VISIBLE
        }

        //we remove the scroll listener to avoid it to try to load more items
        mRecyclerView.removeOnScrollListener(mScrollListener)
        mAdapter.removeFooter()

    }

    override fun clearVisibleList() {
        mItems.clear()
        mAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {

        if(mItems.size > 0) {
            mAdapter.showFooterProgress()
        } else {

            mProgressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {

        mProgressBar.visibility = View.GONE

    }

    override fun showError() {

        if(mItems.size > 0) {
            mAdapter.showFooterError()
        } else {
            mBtnTryAgain.visibility = View.VISIBLE
            mTvError.visibility = View.VISIBLE
        }

    }

    //**************************************************************************
    // INNER METHODS
    //**************************************************************************

    fun loadOpenRepositories() {

        //we don't call the presenter if is already doing something
        if(mLoadPullRequestsJob != null && mLoadPullRequestsJob!!.isActive) {
            return
        }

        mLoadPullRequestsJob = launch(UI) {
            mPresenter.loadOpenPullRequests(mOwnerLogin, mRepoName, mItems.size)
        }
    }

    fun onPullRequestClicked(pullRequest: PullRequest) {
        val it = Intent(Intent.ACTION_VIEW).setData(Uri.parse(pullRequest.htmlUrl))
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(it);
    }

    private fun initViews() {

        /* because we're using the lazy init, we need to use our recyclerView for the objects
      initialize, otherwise we could simple put this method into the [setupRecyclerView()] */
        mRecyclerView.addOnScrollListener(mScrollListener)

        setSupportActionBar(mToolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mCtlToolbar.title = getString(R.string.title_pull_requests, mRepoName)

        mBtnTryAgain.setOnClickListener({
            loadOpenRepositories()
        })
    }

    private fun restoreInstance(savedInstanceState: Bundle) {

        mListState = savedInstanceState.getParcelable(LIST_STATE_KEY)

        val items = savedInstanceState.getParcelableArrayList<PullRequest>(ITEMS_STATE_KEY)

        if(items != null) {
            mItems.clear()
            mItems.addAll(items)
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