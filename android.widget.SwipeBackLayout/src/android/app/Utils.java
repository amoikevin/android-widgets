package android.app;

import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by Chaojun Wang on 6/9/14.
 */
public class Utils
{
	private Utils()
	{
	}

	/**
	 * Convert a translucent themed Activity {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
	 * Activity.
	 * <p>
	 * Call this whenever the background of a translucent Activity has changed to become opaque. Doing so will allow the {@link android.view.Surface} of the Activity behind to be released.
	 * <p>
	 * This call has no effect on non-translucent activities or on activities with the {@link android.R.attr#windowIsFloating} attribute.
	 */
	public static void convertActivityFromTranslucent(final Activity activity)
	{
		try
		{
			final Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
			method.setAccessible(true);
			method.invoke(activity);
		}
		catch (final Throwable t)
		{
		}
	}

	/**
	 * Convert a translucent themed Activity {@link android.R.attr#windowIsTranslucent} back from opaque to
	 * translucent following a call to {@link #convertActivityFromTranslucent(android.app.Activity)} .
	 * <p>
	 * Calling this allows the Activity behind this one to be seen again. Once all such Activities have been redrawn
	 * <p>
	 * This call has no effect on non-translucent activities or on activities with the {@link android.R.attr#windowIsFloating} attribute.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void convertActivityToTranslucent(final Activity activity)
	{
		try
		{
			final Class<?>[] classes = Activity.class.getDeclaredClasses();
			Class<?> translucentConversionListenerClazz = null;
			for (final Class clazz : classes)
				if (clazz.getSimpleName().contains("TranslucentConversionListener"))
					translucentConversionListenerClazz = clazz;
			if (Build.VERSION.SDK_INT < 21)
			{
				final Method method = Activity.class.getDeclaredMethod(
						"convertToTranslucent",
						translucentConversionListenerClazz);
				method.setAccessible(true);
				method.invoke(activity, new Object[] { null });
			}
			else
			{
				final Method method = Activity.class.getDeclaredMethod(
						"convertToTranslucent",
						translucentConversionListenerClazz,
						ActivityOptions.class);
				method.setAccessible(true);
				method.invoke(activity, new Object[] { null, null });
			}
		}
		catch (final Throwable t)
		{
		}
	}
}
