package com.igorvd.githubrepo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.igorvd.githubrepo.data.GitHubRepo
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposContract
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposPresenter
import com.igorvd.githubrepo.utils.EndlessRecyclerViewScrollListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), PopularReposContract.View {

    private val LIST_STATE_KEY = "recycler_state"
    private var listState : Parcelable? = null

    @Inject
    lateinit var mPresenter : PopularReposContract.Presenter

    private lateinit var mProgressBar : ProgressBar

    //recycler view objects
    private val mRecyclerView : RecyclerView by lazy {
        setupRecyclerView()
    }

    private val mLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this) }
    private val mAdapter : ListReposAdapter by lazy { ListReposAdapter(this, mItems) }
    private lateinit var mScrollListener : EndlessRecyclerViewScrollListener
    private val mItems : ArrayList<GitHubRepo> by lazy {ArrayList<GitHubRepo>()}

    private var mLoadRepositoriesJob : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mScrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                launch(UI) {
                    mPresenter.loadRepositories()
                }
            }
        }

        mRecyclerView.addOnScrollListener(mScrollListener)

        mProgressBar = progressBar as ProgressBar

        println("On create");

        mLoadRepositoriesJob = launch(UI) {
            mPresenter.loadRepositories()
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

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)

        listState = mLayoutManager.onSaveInstanceState()
        outState?.putParcelable(LIST_STATE_KEY, listState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        listState = savedInstanceState?.getParcelable(LIST_STATE_KEY)
    }

    //view interface

    override fun showRepositories(repositories: List<GitHubRepo>) {

        println("Repos loaded")

        mItems.addAll(repositories)
        mAdapter.notifyDataSetChanged()

    }

    override fun showProgress() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgressBar.visibility = View.GONE
    }

    override fun showError() {
        mainTv.text = "exception"
    }

    //inner methods

    private fun setupRecyclerView() : RecyclerView {

        mainRv.layoutManager = mLayoutManager
        mainRv.adapter = mAdapter
        mainRv.itemAnimator = DefaultItemAnimator()

        return mainRv
    }
}
