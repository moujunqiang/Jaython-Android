/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tiny.loader.internal.core.listener;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ListView;

import com.tiny.loader.internal.core.ImageLoader;

/**
 * Listener-helper for {@linkplain AbsListView list views} ({@link ListView}, {@link GridView}) which can
 * {@linkplain ImageLoader#pause() pause TinyImageLoader's tasks} while list view is scrolling (touch scrolling and/or
 * fling). It prevents redundant loadings.<br />
 * Set it to your list view's {@link AbsListView#setOnScrollListener(OnScrollListener) setOnScrollListener(...)}.<br />
 * This listener can wrap your custom {@linkplain OnScrollListener listener}.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.7.0
 */
public class PauseOnScrollListener implements OnScrollListener {

    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final OnScrollListener externalListener;
    private ImageLoader imageLoader;

    /**
     * Constructor
     *
     * @param imageLoader   {@linkplain ImageLoader} instance for controlling
     * @param pauseOnScroll Whether {@linkplain ImageLoader#pause() pause TinyImageLoader} during touch scrolling
     * @param pauseOnFling  Whether {@linkplain ImageLoader#pause() pause TinyImageLoader} during fling
     */
    public PauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageLoader, pauseOnScroll, pauseOnFling, null);
    }

    /**
     * Constructor
     *
     * @param imageLoader    {@linkplain ImageLoader} instance for controlling
     * @param pauseOnScroll  Whether {@linkplain ImageLoader#pause() pause TinyImageLoader} during touch scrolling
     * @param pauseOnFling   Whether {@linkplain ImageLoader#pause() pause TinyImageLoader} during fling
     * @param customListener Your custom {@link OnScrollListener} for {@linkplain AbsListView list view} which also
     *                       will be get scroll events
     */
    public PauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling,
                                 OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                imageLoader.resume();
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (pauseOnScroll) {
                    imageLoader.pause();
                }
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                if (pauseOnFling) {
                    imageLoader.pause();
                }
                break;
        }
        if (externalListener != null) {
            externalListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (externalListener != null) {
            externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}