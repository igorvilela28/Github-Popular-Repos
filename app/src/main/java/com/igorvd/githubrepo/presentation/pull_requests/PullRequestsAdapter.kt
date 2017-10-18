package com.igorvd.githubrepo.presentation.pull_requests

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.igorvd.githubrepo.R
import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.utils.extensions.formatDate
import com.igorvd.githubrepo.utils.extensions.loadImageFromUrl
import kotlinx.android.synthetic.main.item_progress.view.*
import kotlinx.android.synthetic.main.pull_requests_item.view.*

/**
 * @author Igor Vilela
 * @since 14/10/17
 */
class PullRequestsAdapter(
        val context: Context,
        val pullRequests: List<PullRequest>,
        val onItemClicked: (PullRequest) -> Unit,
        val onRetryClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class MyFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //constants for footer types
    private val TYPE_LOADING = 0
    private val TYPE_ERROR = 1
    private var currentFootType = TYPE_LOADING

    //view types
    private val ITEMS = 0
    private val LOAD_MORE = 1

    //public variables
    var hasFooter = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == ITEMS) {
            val itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pull_requests_item, parent, false)

            MyViewHolder(itemView)
        } else {

            val itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_progress, parent, false)

            MyFooterViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == pullRequests.size) {
            LOAD_MORE
        } else {
            ITEMS
        }
    }

    override fun getItemCount(): Int = pullRequests.size + 1

    override fun getItemId(position: Int): Long {

        return if(position == pullRequests.size) {

            Long.MAX_VALUE

        } else {
            pullRequests.get(position).id.toLong()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is MyViewHolder) {
            bindItemViewHolder(holder, position)
        } else if (holder is MyFooterViewHolder) {
            bindFooterViewHolder(holder)
        }
    }

    /**
     * Shows a progress bar below the last item
     */
    fun showFooterProgress() {

        setFooterType(TYPE_LOADING)
    }

    /**
     * shows a error message below the last item
     */
    fun showFooterError() {
        setFooterType(TYPE_ERROR)
    }

    /**
     * Remove the progress or error message below the last item
     */
    fun removeFooter() {
        this.hasFooter = false
        notifyDataSetChanged()
    }

    //**************************************************************************
    // INNER METHODS
    //**************************************************************************

    /**
     * Change the footer type
     * @param[type] Must be one of [TYPE_ERROR] or [TYPE_LOADING]
     */
    private fun setFooterType(type: Int) {
        this.hasFooter = true
        this.currentFootType = type
        notifyDataSetChanged()
    }

    private fun bindItemViewHolder(holder: MyViewHolder, position: Int) {
        val pullRequest: PullRequest = pullRequests.get(position)

        holder.itemView.pullRequestTvTitle.text = pullRequest.title
        holder.itemView.pullRequestTvDescription.text = pullRequest.body
        holder.itemView.pullRequestTvUsername.text = pullRequest.owner.login
        holder.itemView.pullRequestIvAvatar.loadImageFromUrl(pullRequest.owner.avatarUrl)
        holder.itemView.pullRequestTvDate.setText(pullRequest.createdAt.formatDate())
        holder.itemView.setOnClickListener { onItemClicked.invoke(pullRequest) }
    }

    private fun bindFooterViewHolder(holder: MyFooterViewHolder) {

        val visibility = if(hasFooter) View.VISIBLE else View.GONE
        holder.itemView.progressRoot.visibility = visibility

        if(currentFootType == TYPE_LOADING) {

            holder.itemView.itemProgressLLRetry.visibility = View.GONE
            holder.itemView.itemprogressIvRetryIcon.visibility = View.GONE
            holder.itemView.itemProgressBar.visibility = View.VISIBLE

            holder.itemView.progressRoot.setOnClickListener {  }

        } else if (currentFootType == TYPE_ERROR) {

            holder.itemView.itemProgressBar.visibility = View.GONE
            holder.itemView.itemProgressLLRetry.visibility = View.VISIBLE
            holder.itemView.itemprogressIvRetryIcon.visibility = View.VISIBLE

            holder.itemView.progressRoot.setOnClickListener {
                onRetryClick.invoke()
            }

        }
    }


}