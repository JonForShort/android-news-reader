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
package com.github.jonforshort.newsreader.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jonforshort.newsreader.feedcontentfetcher.FeedContent
import com.github.jonforshort.newsreader.feedcontentfetcher.FeedContentFetcher
import com.github.jonforshort.newsreader.feedcontentfetcher.FeedContentFetcher.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

internal class FeedViewModel : ViewModel() {

    private val feedContent = MutableLiveData<FeedContent>()

    private val feedUrls = MutableLiveData<List<URL>>()

    fun refreshFeedContent() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                feedUrls.value?.forEach { url ->
                    val fetchResult = FeedContentFetcher(url).fetch()
                    if (fetchResult is Result.Success) {
                        feedContent.postValue(fetchResult.result)
                    }
                }
            }
        }
    }

    fun getFeedContentLiveData() = feedContent

    fun getFeedUrls() = feedUrls
}