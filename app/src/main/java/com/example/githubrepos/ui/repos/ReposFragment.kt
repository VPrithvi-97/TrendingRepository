package com.example.githubrepos.ui.repos

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import com.example.githubrepos.R
import com.example.githubrepos.base.BaseFragment
import com.example.githubrepos.databinding.FragmentReposBinding
import com.example.githubrepos.extension_functions.hide
import com.example.githubrepos.extension_functions.show
import com.example.githubrepos.model.Item
import com.example.githubrepos.model.Languages
import com.example.githubrepos.ui.repos.adapter.ReposAdapter
import com.example.githubrepos.ui.repos.adapter.ReposLoadStateAdapter
import java.util.*
import kotlin.collections.ArrayList

class ReposFragment : BaseFragment<FragmentReposBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_repos

    private val viewModel by viewModels<ReposViewModel>()
    private lateinit var reposAdapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reposAdapter = ReposAdapter(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        dataBinding.apply {
            bViewModel = viewModel
            bLifecycleScope = lifecycleScope
            rvRepos.adapter = reposAdapter.withLoadStateFooter(
                ReposLoadStateAdapter(
                    lifecycleScope,
                    reposAdapter::retry
                )
            )

            setViewListeners()
            setDataObservers()
            setHasOptionsMenu(true)
        }

    }

    private fun FragmentReposBinding.setViewListeners(){
        btnRetry.setOnClickListener {
            setDataObservers()
        }

        reposAdapter.addLoadStateListener {
            if (isDataBindingActive){
                dataBinding.swipeRefreshLayout.isRefreshing = false
                // when we have an internet connection but no items to present then show me an empty list
                val isListEmpty = it.refresh is LoadState.NotLoading && reposAdapter.itemCount == 0
                val loadingFirstTime = it.mediator?.refresh is LoadState.Loading
                val error = it.refresh is LoadState.Error && reposAdapter.itemCount == 0
                when{
                    loadingFirstTime -> showLoadingUi()
                    isListEmpty || error -> showErrorUi(isListEmpty)
                    else -> showSuccessUi()
                }
            }
        }

        reposAdapter.clickListener = {model, position, viewId ->
            reposAdapter.retry()
        }
    }

    private fun setDataObservers(){
        viewModel.repos.observe(viewLifecycleOwner) {
            reposAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        val searchAutoComplete: SearchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        // Get SearchView autocomplete object
        searchAutoComplete.setTextColor(Color.WHITE)
        searchAutoComplete.setDropDownBackgroundResource(R.color.purple_200)

        val newsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            Languages.data
        )
        searchAutoComplete.setAdapter(newsAdapter)

        // Listen to search view item on click event
        searchAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, itemIndex, _ ->
                val queryString = adapterView.getItemAtPosition(itemIndex) as String
                searchAutoComplete.setText(
                    String.format(
                        getString(R.string.search_query),
                        queryString
                    )
                )
                dataBinding.rvRepos.scrollToPosition(0)
                val languageQuery = String.format(getString(R.string.query), queryString)
                viewModel.searchRepos(languageQuery)
                searchView.clearFocus()
                (activity as AppCompatActivity).supportActionBar?.title = queryString.capitalize(
                    Locale.ROOT
                )
            }
    }


    private fun FragmentReposBinding.showLoadingUi(){
        rvRepos.hide()
        tvEmptyData.hide()
        tvErrorData.hide()
        progressBar.show()
        btnRetry.hide()
    }

    private fun FragmentReposBinding.showErrorUi(isListEmpty: Boolean){
        progressBar.hide()
        tvEmptyData.show()
        rvRepos.hide()
        btnRetry.hide()
    }

    private fun FragmentReposBinding.showSuccessUi(){
        progressBar.hide()
        tvEmptyData.hide()
        tvErrorData.hide()
        rvRepos.show()
        btnRetry.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}