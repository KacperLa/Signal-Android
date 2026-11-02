package org.thoughtcrime.securesms.conversation.colors.ui.custom

import androidx.core.graphics.ColorUtils
import org.thoughtcrime.securesms.wallpaper.ChatWallpaper
import java.util.EnumMap
import kotlin.math.roundToInt

data class CustomChatColorCreatorState(
  val loading: Boolean,
  val wallpaper: ChatWallpaper?,
  val sliderStates: EnumMap<CustomChatColorEdge, ColorSlidersState>,
  val selectedEdge: CustomChatColorEdge,
  val degrees: Float
)

/** Brightness is HSL lightness, preserving the existing picker's hue/saturation model. */
data class ColorSlidersState(val huePosition: Int, val saturationPosition: Int, val brightnessPosition: Int) {
  fun toColor(max: Int): Int {
    require(max > 0)
    return ColorUtils.HSLToColor(
      floatArrayOf(
        huePosition.coerceIn(0, max) * 360f / max,
        saturationPosition.coerceIn(0, max).toFloat() / max,
        brightnessPosition.coerceIn(0, max).toFloat() / max
      )
    )
  }

  companion object {
    fun fromColor(color: Int, max: Int): ColorSlidersState {
      require(max > 0)
      val hsl = FloatArray(3)
      ColorUtils.colorToHSL(color, hsl)
      return ColorSlidersState(
        (hsl[0] / 360f * max).roundToInt(),
        (hsl[1] * max).roundToInt(),
        (hsl[2] * max).roundToInt()
      )
    }
  }
}
