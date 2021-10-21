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
package com.github.jonforshort.rssreader.ui

import android.content.Context
import com.github.jonforshort.rssreader.feedcontentrepo.FeedContentRepository
import com.github.jonforshort.rssreader.feedsource.repo.createFeedRepo
import com.github.jonforshort.rssreader.preferences.UserPreferences
import com.github.jonforshort.rssreader.ui.main.feedArticle.database.createFeedArticleRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class UIModule {

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context) = UserPreferences(context)

    @Singleton
    @Provides
    fun provideFeedContentRepository(@ApplicationContext context: Context) = FeedContentRepository(context)

    @Singleton
    @Provides
    fun provideFeedRepository() = createFeedRepo()

    @Singleton
    @Provides
    fun provideFeedArticleRepository(@ApplicationContext context: Context) = createFeedArticleRepo(context)
}
