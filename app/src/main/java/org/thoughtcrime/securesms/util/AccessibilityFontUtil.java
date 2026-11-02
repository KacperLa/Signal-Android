/*
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.thoughtcrime.securesms.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;

import androidx.annotation.NonNull;

/** Keeps message font sizing independent from the interface-only font preference. */
public final class AccessibilityFontUtil {
  private AccessibilityFontUtil() {}

  public static float interfaceFontScale(float systemScale, int percent) {
    return systemScale * Math.max(100, Math.min(150, percent)) / 100f;
  }

  /** Retain the current window's density and configuration, but only the system font scale. */
  public static @NonNull Context messageFontContext(@NonNull Context context) {
    Configuration configuration = new Configuration(context.getResources().getConfiguration());
    configuration.fontScale = context.getApplicationContext().getResources().getConfiguration().fontScale;
    return context.createConfigurationContext(configuration);
  }

  /** Uses Android's SP conversion (including nonlinear accessibility scaling on newer Android). */
  public static float messageTextSizePx(@NonNull Context context, float sp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, messageFontContext(context).getResources().getDisplayMetrics());
  }
}
