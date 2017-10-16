package com.igorvd.githubrepo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import android.view.LayoutInflater
import com.igorvd.githubrepo.data.GitHubRepo


/**
 * @author Igor Vilela
 * @since 14/10/17
 */
class ListReposAdapter (
        val context: Context,
        val repos: List<GitHubRepo>) : RecyclerView.Adapter<ListReposAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tvTitle: TextView
        lateinit var tvDescription: TextView
        lateinit var tvStars: TextView
        lateinit var tvForks: TextView
        lateinit var tvUsername: TextView
        lateinit var tvFullname: TextView
        lateinit var ivAvatar: TextView
        lateinit var ivForks: TextView
        lateinit var ivStars: TextView

        init {
            tvTitle = itemView.findViewById(R.id.repoListTvTitle)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_repos_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val repo: GitHubRepo = repos.get(position)

        holder.tvTitle.text = repo.fullName

    }




}