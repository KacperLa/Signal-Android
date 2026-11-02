/*
 * Copyright 2026 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.signal.appsettings.appearance

import kotlin.math.roundToInt

/** Preserve upstream quote sizes at 14/16/22/28 without rewriting custom message sizes. */
object MessageFontSizes {
  const val MIN = 14
  const val MAX = 42

  fun quoteSize(messageSize: Int): Int {
    val size = messageSize.coerceIn(MIN, MAX)
    return if (size <= 16) size - 2 else (14f + (size - 16) * (2f / 3f)).roundToInt()
  }
}
