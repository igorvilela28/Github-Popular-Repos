package com.igorvd.githubrepo.presentation.popular_repos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import android.view.LayoutInflater
import com.igorvd.githubrepo.R
import com.igorvd.githubrepo.data.GitHubRepo
import com.igorvd.githubrepo.utils.extensions.loadImageFromUrl
import kotlinx.android.synthetic.main.popular_repos_item.view.*


/**
 * @author Igor Vilela
 * @since 14/10/17
 */
class PopularReposAdapter(
        val context: Context,
        val repos: List<GitHubRepo>) : RecyclerView.Adapter<PopularReposAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_repos_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = repos.size

    override fun getItemId(position: Int): Long = repos.get(position).id.toLong()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val repo: GitHubRepo = repos.get(position)

        holder.itemView.repoListTvTitle.text = repo.name
        holder.itemView.repoListTvDescription.text = repo.description
        holder.itemView.repoListTvForks.text = repo.forksCount.toString()
        holder.itemView.repoListTvStars.text = repo.stargazersCount.toString()
        holder.itemView.repoListTvUsername.text = repo.owner.login
        //holder.itemView.repoListTvFullname.text = repo.owner.
        holder.itemView.repoListIvAvatar.loadImageFromUrl(repo.owner.avatarUrl)

    }




}