/*
 * Copyright 2015 chenupt
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

package android.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.FixedSpeedScroller;

/**
 * Created by chenupt@gmail.com on 2015/3/7.
 * Description TODO
 */
public class ScrollerViewPager extends ViewPager
{

	private static final String TAG = ScrollerViewPager.class.getSimpleName();

	private int duration = 1000;

	public ScrollerViewPager(final Context context)
	{
		super(context);
	}

	public ScrollerViewPager(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void fixScrollSpeed()
	{
		fixScrollSpeed(duration);
	}

	public void fixScrollSpeed(final int duration)
	{
		this.duration = duration;
		setScrollSpeedUsingRefection(duration);
	}

	private void setScrollSpeedUsingRefection(final int duration)
	{
		try
		{
			final Field localField = ViewPager.class.getDeclaredField("mScroller");
			localField.setAccessible(true);
			final FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new DecelerateInterpolator(1.5F));
			scroller.setDuration(duration);
			localField.set(this, scroller);
			return;
		}
		catch (final IllegalAccessException localIllegalAccessException)
		{
		}
		catch (final IllegalArgumentException localIllegalArgumentException)
		{
		}
		catch (final NoSuchFieldException localNoSuchFieldException)
		{
		}
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev)
	{
		try
		{
			return super.onInterceptTouchEvent(ev);
		}
		catch (final IllegalArgumentException e)
		{
			Log.e(TAG, "onInterceptTouchEvent in IllegalArgumentException");
			return false;
		}
	}
}
