/*
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.thoughtcrime.securesms.conversation.colors.ui.custom

import android.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ColorSlidersStateTest {
  private val max = 1023

  @Test
  fun `brightness reaches black and white at every hue`() {
    for (hue in 0..max step 31) {
      assertEquals(Color.BLACK, ColorSlidersState(hue, max, 0).toColor(max))
      assertEquals(Color.WHITE, ColorSlidersState(hue, max, max).toColor(max))
    }
  }

  @Test
  fun `editing existing colors preserves lightness within slider quantization`() {
    for (color in listOf(Color.BLACK, Color.WHITE, Color.RED, Color.GRAY, 0xff123456.toInt(), 0xffabcdef.toInt(), 0xffe5ca8a.toInt())) {
      val restored = ColorSlidersState.fromColor(color, max).toColor(max)
      assertTrue(kotlin.math.abs(Color.red(color) - Color.red(restored)) <= 1)
      assertTrue(kotlin.math.abs(Color.green(color) - Color.green(restored)) <= 1)
      assertTrue(kotlin.math.abs(Color.blue(color) - Color.blue(restored)) <= 1)
    }
  }

  @Test
  fun `gradient edges have independent brightness`() {
    val top = ColorSlidersState(200, 800, 400)
    val bottom = top.copy(brightnessPosition = max)
    assertEquals(400, top.brightnessPosition)
    assertEquals(Color.WHITE, bottom.toColor(max))
  }

  @Test
  fun `out of range positions clamp safely`() {
    assertEquals(Color.BLACK, ColorSlidersState(-1, -1, -1).toColor(max))
    assertEquals(Color.WHITE, ColorSlidersState(max + 1, max + 1, max + 1).toColor(max))
  }
}
