/*
 * Copyright (C) 2012 The Android Open Source Project
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

package android.widget;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Provides static functions to work with views
 */
public class ViewUtil
{
	private ViewUtil()
	{
	}

	/**
	 * Returns a boolean indicating whether or not the view's layout direction is RTL
	 *
	 * @param view
	 *        - A valid view
	 * @return True if the view's layout direction is RTL
	 */
	public static boolean isViewLayoutRtl(View view)
	{
		return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL;
	}
}
