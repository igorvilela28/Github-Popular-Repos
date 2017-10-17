package com.igorvd.githubrepo.presentation.popular_repos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.igorvd.githubrepo.R
import com.igorvd.githubrepo.data.GitHubRepo
import com.igorvd.githubrepo.presentation.EXTRA_OWNER_LOGIN
import com.igorvd.githubrepo.presentation.EXTRA_REPO_NAME
import com.igorvd.githubrepo.presentation.pull_requests.PullRequestsActivity
import com.igorvd.githubrepo.utils.EndlessRecyclerViewScrollListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_popular_repos.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

class PopularReposActivity : AppCompatActivity(), PopularReposContract.View {

    private val LIST_STATE_KEY = "recycler_state"
    private val ITEMS_STATE_KEY = "items_state"
    private var listState : Parcelable? = null

    @Inject
    lateinit var mPresenter : PopularReposContract.Presenter

    //view objects
    private val mProgressBar : ProgressBar by lazy { progressBar }

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


        /* because we're using the lazy init, we need to use our recyclerView for the objects
        initialize, otherwise we could simple put this method into the [setupRecyclerView()] */
        mRecyclerView.addOnScrollListener(mScrollListener)

        println("On create");

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
        outState?.putParcelableArrayList(ITEMS_STATE_KEY, mItems)

    }

    //**************************************************************************
    // VIEW INTERFACE
    //**************************************************************************

    override fun showRepositories(repositories: List<GitHubRepo>) {

        println("Repos loaded")

        if(mAdapter.hasFooter) {
            mAdapter.removeFooter()
        }

        mItems.addAll(repositories)
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

    private fun restoreInstance(savedInstanceState: Bundle) {

        listState = savedInstanceState.getParcelable(LIST_STATE_KEY)

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
