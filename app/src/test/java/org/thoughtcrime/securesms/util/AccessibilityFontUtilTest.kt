/*
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.thoughtcrime.securesms.util

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [33, 34])
class AccessibilityFontUtilTest {
  @Test
  fun `interface scale respects system setting and clamps preference`() {
    assertEquals(2f, AccessibilityFontUtil.interfaceFontScale(2f, 100), 0.0001f)
    assertEquals(3f, AccessibilityFontUtil.interfaceFontScale(2f, 150), 0.0001f)
    assertEquals(2f, AccessibilityFontUtil.interfaceFontScale(2f, 0), 0.0001f)
    assertEquals(3f, AccessibilityFontUtil.interfaceFontScale(2f, 200), 0.0001f)
  }

  @Test
  fun `message conversion uses system font scale without changing window density`() {
    val application = ApplicationProvider.getApplicationContext<Context>()
    val original = Configuration(application.resources.configuration)
    val configuration = Configuration(original).apply {
      fontScale = original.fontScale * 1.5f
      densityDpi = 320
    }
    val baseline = application.createConfigurationContext(Configuration(original).apply { densityDpi = 320 })
    val activityContext = application.createConfigurationContext(configuration)
    val messageContext = AccessibilityFontUtil.messageFontContext(activityContext)

    assertEquals(original.fontScale, messageContext.resources.configuration.fontScale, 0.0001f)
    assertEquals(320, messageContext.resources.configuration.densityDpi)
    assertEquals(configuration.fontScale, activityContext.resources.configuration.fontScale, 0.0001f)
    assertEquals(original.fontScale, application.resources.configuration.fontScale, 0.0001f)
    assertEquals(
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 42f, baseline.resources.displayMetrics),
      AccessibilityFontUtil.messageTextSizePx(activityContext, 42f),
      0.0001f
    )
  }
  @Suppress("DEPRECATION")
  @Test
  fun `large system font conversion remains independent from interface preference`() {
    val application = ApplicationProvider.getApplicationContext<Context>()
    val resources = application.resources
    val original = Configuration(resources.configuration)
    try {
      resources.updateConfiguration(Configuration(original).apply { fontScale = 2f }, resources.displayMetrics)
      val systemContext = application.createConfigurationContext(Configuration(resources.configuration))
      val interfaceContext = application.createConfigurationContext(Configuration(resources.configuration).apply { fontScale = 3f })
      for (sp in listOf(14f, 16f, 28f, 42f)) {
        assertEquals(
          TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, systemContext.resources.displayMetrics),
          AccessibilityFontUtil.messageTextSizePx(interfaceContext, sp),
          0.0001f
        )
      }
    } finally {
      resources.updateConfiguration(original, resources.displayMetrics)
    }
  }

}
