package android.app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SwipeBackLayout;
import android.widget.swipebacklayout.R;

/**
 * @author Yrom
 */
public class SwipeBackActivityHelper
{
	private final Activity mActivity;

	private SwipeBackLayout mSwipeBackLayout;

	public SwipeBackActivityHelper(final Activity activity)
	{
		mActivity = activity;
	}

	@SuppressWarnings("deprecation")
	public void onActivityCreate()
	{
		mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
		mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(
				R.layout.swipeback_layout, null);
		mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener()
		{
			@Override
			public void onScrollStateChange(final int state, final float scrollPercent)
			{
			}

			@Override
			public void onEdgeTouch(final int edgeFlag)
			{
				Utils.convertActivityToTranslucent(mActivity);
			}

			@Override
			public void onScrollOverThreshold()
			{

			}
		});
	}

	public void onPostCreate()
	{
		mSwipeBackLayout.attachToActivity(mActivity);
	}

	public View findViewById(final int id)
	{
		if (mSwipeBackLayout != null)
			return mSwipeBackLayout.findViewById(id);
		return null;
	}

	public SwipeBackLayout getSwipeBackLayout()
	{
		return mSwipeBackLayout;
	}
}
