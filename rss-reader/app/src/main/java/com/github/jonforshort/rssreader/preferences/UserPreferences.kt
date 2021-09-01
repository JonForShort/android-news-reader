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
package com.github.jonforshort.rssreader.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.jonforshort.rssreader.preferences.UserPreferences.PreferenceKeys.SELECTED_FEED_TAGS
import kotlinx.coroutines.flow.map

internal class UserPreferences(private val context: Context) {

    val selectedFeedTagsFlow = context.dataStore.data
        .map { preferences -> preferences[SELECTED_FEED_TAGS] ?: emptySet() }

    suspend fun setSelectedFeedTags(feedTags: Set<String>) = context.dataStore
        .edit { preferences -> preferences[SELECTED_FEED_TAGS] = feedTags }

    private object PreferenceKeys {
        val SELECTED_FEED_TAGS = stringSetPreferencesKey("selected_feed_tags")
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPreferences")