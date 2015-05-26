package android.preference;

import android.app.SwipeBackableActivity;
import android.app.SwipeBackActivityHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.SwipeBackLayout;

public class SwipeBackPreferenceActivity extends PreferenceActivity implements SwipeBackableActivity
{
	private SwipeBackActivityHelper mHelper;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(final int id)
	{
		final View v = super.findViewById(id);
		if (v == null && mHelper != null)
			return mHelper.findViewById(id);
		return v;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout()
	{
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(final boolean enable)
	{
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity()
	{
		getSwipeBackLayout().scrollToFinishActivity();
	}
}
