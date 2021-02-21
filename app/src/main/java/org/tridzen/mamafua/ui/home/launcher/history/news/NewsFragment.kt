package org.tridzen.mamafua.ui.home.launcher.history.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.News
import org.tridzen.mamafua.data.remote.network.withdi.Resource
import org.tridzen.mamafua.databinding.FragmentNewsBinding
import org.tridzen.mamafua.utils.hideView
import org.tridzen.mamafua.utils.runLayoutAnimation
import org.tridzen.mamafua.utils.showSnackBar

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {

    private lateinit var newsAdapter: NewsAdapter
    private val viewModel by viewModels<NewsViewModel>()
    private lateinit var binding: FragmentNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        viewModel.characters.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) setUpRv(it.data)
                }
                Resource.Status.ERROR ->
                    it.message?.let { it1 -> view.showSnackBar(it1) }

                Resource.Status.LOADING -> {
                    hideView(binding.lavActivity, binding.mtvActivity, condition = true)
                }
            }
        })
    }

    private fun setUpRv(list: List<News>) {
        newsAdapter = NewsAdapter()
        newsAdapter.submitList(list)
//        hideView(binding.lavActivity, binding.mtvActivity, condition = list.isEmpty())
        rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = newsAdapter
            runLayoutAnimation()
        }
    }
}