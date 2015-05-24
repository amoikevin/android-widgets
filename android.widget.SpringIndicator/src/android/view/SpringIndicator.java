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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v1.animation.ArgbEvaluator;
import android.support.v1.animation.ObjectAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.springindicator.R;

/**
 * Created by chenupt@gmail.com on 2015/1/31.
 * Description : Tab layout container
 */
public class SpringIndicator extends FrameLayout
{

	private static final int INDICATOR_ANIM_DURATION = 3000;

	private final float acceleration = 0.5f;
	private final float headMoveOffset = 0.6f;
	private final float footMoveOffset = 1 - headMoveOffset;
	private float radiusMax;
	private float radiusMin;
	private float radiusOffset;

	private float textSize;
	private int textColorId;
	private int textBgResId;
	private int selectedTextColorId;
	private int indicatorColorId;
	private int indicatorColorsId;
	private int[] indicatorColorArray;

	private LinearLayout tabContainer;
	private SpringView springView;
	private ViewPager viewPager;

	private List<TextView> tabs;

	private ViewPager.OnPageChangeListener delegateListener;
	private TabClickListener tabClickListener;
	private ObjectAnimator indicatorColorAnim;

	public SpringIndicator(final Context context)
	{
		this(context, null);
	}

	public SpringIndicator(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
		initAttrs(attrs);
	}

	private void initAttrs(final AttributeSet attrs)
	{
		textColorId = R.color.springIndicator_default_text_color;
		selectedTextColorId = R.color.springIndicator_default_text_color_selected;
		indicatorColorId = R.color.springIndicator_default_indicator_bg;
		textSize = getResources().getDimension(R.dimen.springIndicator_default_text_size);
		radiusMax = getResources().getDimension(R.dimen.springIndicator_default_radius_max);
		radiusMin = getResources().getDimension(R.dimen.springIndicator_default_radius_min);

		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SpringIndicator);
		textColorId = a.getResourceId(R.styleable.SpringIndicator_textColor, textColorId);
		selectedTextColorId = a.getResourceId(R.styleable.SpringIndicator_selectedTextColor, selectedTextColorId);
		textSize = a.getDimension(R.styleable.SpringIndicator_textSize, textSize);
		textBgResId = a.getResourceId(R.styleable.SpringIndicator_textBackground, 0);
		indicatorColorId = a.getResourceId(R.styleable.SpringIndicator_indicatorColor, indicatorColorId);
		indicatorColorsId = a.getResourceId(R.styleable.SpringIndicator_indicatorColors, 0);
		radiusMax = a.getDimension(R.styleable.SpringIndicator_maxRadius, radiusMax);
		radiusMin = a.getDimension(R.styleable.SpringIndicator_minRadius, radiusMin);
		a.recycle();

		if (indicatorColorsId != 0)
			indicatorColorArray = getResources().getIntArray(indicatorColorsId);
		radiusOffset = radiusMax - radiusMin;
	}

	public void setViewPager(final ViewPager viewPager)
	{
		this.viewPager = viewPager;
		initSpringView();
		setUpListener();
	}

	private void initSpringView()
	{
		addPointView();
		addTabContainerView();
		addTabItems();
	}

	private void addPointView()
	{
		springView = new SpringView(getContext());
		springView.setIndicatorColor(getResources().getColor(indicatorColorId));
		addView(springView);
	}

	private void addTabContainerView()
	{
		tabContainer = new LinearLayout(getContext());
		tabContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
		tabContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabContainer.setGravity(Gravity.CENTER);
		addView(tabContainer);
	}

	private void addTabItems()
	{
		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
		tabs = new ArrayList<>();
		for (int i = 0; i < viewPager.getAdapter().getCount(); i++)
		{
			final TextView textView = new TextView(getContext());
			if (viewPager.getAdapter().getPageTitle(i) != null)
				textView.setText(viewPager.getAdapter().getPageTitle(i));
			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			textView.setTextColor(getResources().getColor(textColorId));
			if (textBgResId != 0)
				textView.setBackgroundResource(textBgResId);
			textView.setLayoutParams(layoutParams);
			final int position = i;
			textView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					if (tabClickListener == null || tabClickListener.onTabClick(position))
						viewPager.setCurrentItem(position);
				}
			});
			tabs.add(textView);
			tabContainer.addView(textView);
		}
	}

	/**
	 * Set current point position.
	 */
	private void createPoints()
	{
		final View view = tabs.get(viewPager.getCurrentItem());
		springView.getHeadPoint().setX(ViewCompat.getX(view) + view.getWidth() / 2);
		springView.getHeadPoint().setY(ViewCompat.getY(view) + view.getHeight() / 2);
		springView.getFootPoint().setX(ViewCompat.getX(view) + view.getWidth() / 2);
		springView.getFootPoint().setY(ViewCompat.getY(view) + view.getHeight() / 2);
		springView.animCreate();
	}

	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b)
	{
		super.onLayout(changed, l, t, r, b);
		createPoints();
		setSelectedTextColor(viewPager.getCurrentItem());
	}

	private void setUpListener()
	{
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{

			@Override
			public void onPageSelected(final int position)
			{
				super.onPageSelected(position);
				setSelectedTextColor(position);
				if (delegateListener != null)
					delegateListener.onPageSelected(position);
			}

			@Override
			public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels)
			{
				if (position < tabs.size() - 1)
				{
					// radius
					final float radiusOffsetHead = 0.5f;
					if (positionOffset < radiusOffsetHead)
						springView.getHeadPoint().setRadius(radiusMin);
					else
						springView.getHeadPoint()
								.setRadius(
										(positionOffset - radiusOffsetHead) / (1 - radiusOffsetHead) * radiusOffset
												+ radiusMin);
					final float radiusOffsetFoot = 0.5f;
					if (positionOffset < radiusOffsetFoot)
						springView.getFootPoint().setRadius(
								(1 - positionOffset / radiusOffsetFoot) * radiusOffset + radiusMin);
					else
						springView.getFootPoint().setRadius(radiusMin);

					// x
					float headX = 1f;
					if (positionOffset < headMoveOffset)
					{
						final float positionOffsetTemp = positionOffset / headMoveOffset;
						headX = (float) ((Math.atan(positionOffsetTemp * acceleration * 2 - acceleration) + Math
								.atan(acceleration)) / (2 * Math.atan(acceleration)));
					}
					springView.getHeadPoint().setX(getTabX(position) - headX * getPositionDistance(position));
					float footX = 0f;
					if (positionOffset > footMoveOffset)
					{
						final float positionOffsetTemp = (positionOffset - footMoveOffset) / (1 - footMoveOffset);
						footX = (float) ((Math.atan(positionOffsetTemp * acceleration * 2 - acceleration) + Math
								.atan(acceleration)) / (2 * Math.atan(acceleration)));
					}
					springView.getFootPoint().setX(getTabX(position) - footX * getPositionDistance(position));

					// reset radius
					if (positionOffset == 0)
					{
						springView.getHeadPoint().setRadius(radiusMax);
						springView.getFootPoint().setRadius(radiusMax);
					}
				}
				else
				{
					springView.getHeadPoint().setX(getTabX(position));
					springView.getFootPoint().setX(getTabX(position));
					springView.getHeadPoint().setRadius(radiusMax);
					springView.getFootPoint().setRadius(radiusMax);
				}

				// set indicator colors
				// https://github.com/TaurusXi/GuideBackgroundColorAnimation
				if (indicatorColorsId != 0)
				{
					final float length = (position + positionOffset) / viewPager.getAdapter().getCount();
					final int progress = (int) (length * INDICATOR_ANIM_DURATION);
					seek(progress);
				}

				springView.postInvalidate();
				if (delegateListener != null)
					delegateListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}

			@Override
			public void onPageScrollStateChanged(final int state)
			{
				super.onPageScrollStateChanged(state);
				if (delegateListener != null)
					delegateListener.onPageScrollStateChanged(state);
			}
		});
	}

	private float getPositionDistance(final int position)
	{
		final float tarX = ViewCompat.getX(tabs.get(position + 1));
		final float oriX = ViewCompat.getX(tabs.get(position));
		return oriX - tarX;
	}

	private float getTabX(final int position)
	{
		return ViewCompat.getX(tabs.get(position)) + tabs.get(position).getWidth() / 2;
	}

	private void setSelectedTextColor(final int position)
	{
		for (final TextView tab : tabs)
			tab.setTextColor(getResources().getColor(textColorId));
		tabs.get(position).setTextColor(getResources().getColor(selectedTextColorId));
	}

	private void createIndicatorColorAnim()
	{
		indicatorColorAnim = ObjectAnimator.ofInt(springView, "indicatorColor", indicatorColorArray);
		indicatorColorAnim.setEvaluator(new ArgbEvaluator());
		indicatorColorAnim.setDuration(INDICATOR_ANIM_DURATION);
	}

	private void seek(final long seekTime)
	{
		if (indicatorColorAnim == null)
			createIndicatorColorAnim();
		indicatorColorAnim.setCurrentPlayTime(seekTime);
	}

	public List<TextView> getTabs()
	{
		return tabs;
	}

	public void setOnPageChangeListener(final ViewPager.OnPageChangeListener listener)
	{
		this.delegateListener = listener;
	}

	public void setOnTabClickListener(final TabClickListener listener)
	{
		this.tabClickListener = listener;
	}
}
