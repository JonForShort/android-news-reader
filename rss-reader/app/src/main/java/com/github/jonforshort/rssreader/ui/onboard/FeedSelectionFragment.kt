//
// MIT License
//
// Copyright (c) 2021
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package com.github.jonforshort.rssreader.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.jonforshort.rssreader.MainActivity
import com.github.jonforshort.rssreader.R
import com.github.jonforshort.rssreader.databinding.ViewFeedSelectionItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedSelectionFragment : Fragment(), FeedSelectionChangedListener {

    private lateinit var feedSelectionRecyclerView: RecyclerView
    private lateinit var feedSelectionAdapter: FeedSelectionAdapter

    private val feedSelectionViewModel: FeedSelectionViewModel by viewModels()
    private val onboardViewModel: OnboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_onboard_select_feeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedSelectionAdapter = FeedSelectionAdapter(this)
        feedSelectionRecyclerView = view.findViewById(R.id.feedSelectionRecyclerView)
        feedSelectionRecyclerView.adapter = feedSelectionAdapter
        feedSelectionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<Button>(R.id.buttonNext).setOnClickListener {
            lifecycleScope.launch {
                feedSelectionViewModel.saveSelectedFeedTags()
            }
            navigateToMainActivity()
        }

        feedSelectionViewModel.feedTags.observe(viewLifecycleOwner) {
            feedSelectionAdapter.submitList(it)
        }

        lifecycleScope.launch {
            feedSelectionViewModel.refreshFeedTags()
        }
    }

    private fun navigateToMainActivity() {
        activity?.let {
            onboardViewModel.setHasAlreadyOnboarded(true)
            MainActivity.launchMainActivity(it)
        }
    }

    override fun onFeedSelectionChanged(button: CompoundButton, isChecked: Boolean) {
        val tag = button.text.toString()
        if (isChecked) {
            feedSelectionViewModel.selectedFeedTags.add(tag)
        } else {
            feedSelectionViewModel.selectedFeedTags.remove(tag)
        }
    }
}

internal data class FeedSelectionItem(
    val tag: String
)

interface FeedSelectionChangedListener {
    fun onFeedSelectionChanged(button: CompoundButton, isChecked: Boolean)
}

private class FeedSelectionAdapter(
    private val listener: FeedSelectionChangedListener
) : ListAdapter<String, FeedSelectionAdapter.ViewHolder>(FeedTagDiffer()) {

    class ViewHolder(
        private val binding: ViewFeedSelectionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedSelectionItem, listener: FeedSelectionChangedListener) {
            binding.item = item
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val feedSelectionItemBinding = ViewFeedSelectionItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(feedSelectionItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedSelectionItem = getItem(position)
        holder.bind(FeedSelectionItem(feedSelectionItem), listener)
    }

    private class FeedTagDiffer : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}
