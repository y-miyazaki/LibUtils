/*
 * Copyright (C) 2009 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.miya38.triming;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

@SuppressWarnings("javadoc")
public abstract class MonitoredActivity extends Activity {

    private final ArrayList<LifeCycleListener> mListeners = new ArrayList<LifeCycleListener>();

    public static interface LifeCycleListener {
        public void onActivityCreated(MonitoredActivity activity);

        public void onActivityDestroyed(MonitoredActivity activity);

        public void onActivityPaused(MonitoredActivity activity);

        public void onActivityResumed(MonitoredActivity activity);

        public void onActivityStarted(MonitoredActivity activity);

        public void onActivityStopped(MonitoredActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {
        @Override
        public void onActivityCreated(final MonitoredActivity activity) {
        }

        @Override
        public void onActivityDestroyed(final MonitoredActivity activity) {
        }

        @Override
        public void onActivityPaused(final MonitoredActivity activity) {
        }

        @Override
        public void onActivityResumed(final MonitoredActivity activity) {
        }

        @Override
        public void onActivityStarted(final MonitoredActivity activity) {
        }

        @Override
        public void onActivityStopped(final MonitoredActivity activity) {
        }
    }

    public void addLifeCycleListener(final LifeCycleListener listener) {
        if (mListeners.contains(listener)) {
            return;
        }
        mListeners.add(listener);
    }

    public void removeLifeCycleListener(final LifeCycleListener listener) {
        mListeners.remove(listener);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (final LifeCycleListener listener : mListeners) {
            listener.onActivityCreated(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (final LifeCycleListener listener : mListeners) {
            listener.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (final LifeCycleListener listener : mListeners) {
            listener.onActivityStarted(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (final LifeCycleListener listener : mListeners) {
            listener.onActivityStopped(this);
        }
    }
}
