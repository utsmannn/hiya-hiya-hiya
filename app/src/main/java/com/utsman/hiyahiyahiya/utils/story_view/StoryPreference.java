package com.utsman.hiyahiyahiya.utils.story_view;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * MIT License
 *
 * Copyright (c) 2018 bxute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * */
public class StoryPreference {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static final String PREF_NAME = "storyview_cache_pref";
    private static final int PREF_MODE_PRIVATE = 1;

    public StoryPreference(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
    }
    public void clearStoryPreferences() {
        editor.clear();
        editor.apply();
    }

    public void setStoryVisited(String uri){
        editor.putBoolean(uri,true);
        editor.apply();
    }

    public boolean isStoryVisited(String uri){
        return preferences.getBoolean(uri,false);
    }
}
