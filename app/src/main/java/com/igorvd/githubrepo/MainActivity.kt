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
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), PopularReposContract.View {

    private val LIST_STATE_KEY = "recycler_state"
    private var listState : Parcelable? = null

    @Inject
    lateinit var mPresenter : PopularReposContract.Presenter

    private lateinit var mProgressBar : ProgressBar
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter : RecyclerViewAdapter
    private lateinit var mItems : ArrayList<String>
    private lateinit var mScrollListener : EndlessRecyclerViewScrollListener

    var count : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setupRecyclerView()

        mProgressBar = progressBar as ProgressBar

        println("On create");

        launch(UI) {
            mPresenter.loadRepositories()
        }
    }

    override fun onResume() {

        super.onResume()

        if(listState != null) {
            mLayoutManager.onRestoreInstanceState(listState)
        }
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

        mainTv.text = "Repos loaded"

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
}
