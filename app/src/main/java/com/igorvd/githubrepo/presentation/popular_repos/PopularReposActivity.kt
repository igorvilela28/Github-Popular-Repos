package com.igorvd.githubrepo.presentation.popular_repos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.igorvd.githubrepo.R
import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.presentation.EXTRA_OWNER_LOGIN
import com.igorvd.githubrepo.presentation.EXTRA_REPO_NAME
import com.igorvd.githubrepo.presentation.pull_requests.PullRequestsActivity
import com.igorvd.githubrepo.utils.EndlessRecyclerViewScrollListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_popular_repos.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.default_error.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.ArrayList

class PopularReposActivity : AppCompatActivity(), PopularReposContract.View {

    private val LIST_STATE_KEY = "recycler_state"
    private val ITEMS_STATE_KEY = "items_state"
    private var mListState: Parcelable? = null

    @Inject
    lateinit var mPresenter : PopularReposContract.Presenter

    //view objects
    private val mToolbar : Toolbar by lazy { toolbar }
    private val mProgressBar : ProgressBar by lazy { progressBar }
    private val mTvError : TextView by lazy { tvError }
    private val mBtnTryAgain : Button by lazy { btnTryAgain }

    //recycler view objects
    private val mRecyclerView : RecyclerView by lazy { setupRecyclerView() }
    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this) }
    private val mAdapter : PopularReposAdapter by lazy {
        PopularReposAdapter(
                this,
                mItems,
                onItemClicked = {gitHubRepo ->  onRepoClicked(gitHubRepo)},
                onRetryClick = {loadRepositories()})
    }
    private val mScrollListener : EndlessRecyclerViewScrollListener by lazy { createScrollListener() }
    private val mItems : ArrayList<GitHubRepo> by lazy { ArrayList<GitHubRepo>() }

    private var mLoadRepositoriesJob : Job? = null

    //**************************************************************************
    // ACTIVITY LIFE CYCLE
    //**************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_repos)

        initViews()

        if(savedInstanceState != null) {

            restoreInstance(savedInstanceState)

        }

        /**
         * we only start the presenter interaction in the onCreate lifecycle if the items weren't
         * saved by the [onSaveInstanceState] method
         */
        if(mItems.size <= 0) {
            loadRepositories()
        }
    }

    override fun onResume() {

        super.onResume()

        if(mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState)
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

        mListState = mLayoutManager.onSaveInstanceState()
        outState?.putParcelable(LIST_STATE_KEY, mListState)
        outState?.putParcelableArrayList(ITEMS_STATE_KEY, mItems)

    }

    //**************************************************************************
    // VIEW INTERFACE
    //**************************************************************************

    override fun showRepositories(repositories: List<GitHubRepo>) {

        Timber.d("Repos loaded")

        if(mAdapter.hasFooter) {
            mAdapter.removeFooter()
        }

        mItems.addAll(repositories)
        mAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {

        mBtnTryAgain.visibility = View.GONE
        mTvError.visibility = View.GONE

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

    fun loadRepositories() {

        //we don't call the presenter if is already doing something
        if(mLoadRepositoriesJob != null && mLoadRepositoriesJob!!.isActive) {
            return
        }

        mLoadRepositoriesJob = launch(UI) {
            mPresenter.loadRepositories(mItems.size)
        }
    }

    fun onRepoClicked(gitHubRepo: GitHubRepo) {

        val it = Intent(this, PullRequestsActivity::class.java)
        it.putExtra(EXTRA_OWNER_LOGIN, gitHubRepo.owner.login)
        it.putExtra(EXTRA_REPO_NAME, gitHubRepo.name)
        startActivity(it)
    }

    private fun initViews() {

        /* because we're using the lazy init, we need to use our recyclerView for the objects
        initialize, otherwise we could simple put this method into the [setupRecyclerView()] */
        mRecyclerView.addOnScrollListener(mScrollListener)

        setSupportActionBar(mToolbar)
        supportActionBar!!.setHomeButtonEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        mBtnTryAgain.setOnClickListener({
            loadRepositories()
        })

    }

    private fun restoreInstance(savedInstanceState: Bundle) {

        mListState = savedInstanceState.getParcelable(LIST_STATE_KEY)

        val items = savedInstanceState.getParcelableArrayList<GitHubRepo>(ITEMS_STATE_KEY)

        if(items != null) {
            mItems.clear()
            mItems.addAll(items)
        }

    }

    private fun setupRecyclerView() : RecyclerView {

        mainRv.layoutManager = mLayoutManager
        mAdapter.setHasStableIds(true)
        mainRv.adapter = mAdapter
        mainRv.itemAnimator = DefaultItemAnimator()

        return mainRv
    }

    private fun createScrollListener() : EndlessRecyclerViewScrollListener {

        return object : EndlessRecyclerViewScrollListener(mLayoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                loadRepositories()

            }
        }
    }
}
