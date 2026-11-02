/*
 * Copyright 2026 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.signal.appsettings.appearance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageFontSizesTest {
  @Test
  fun `legacy quote sizes are preserved`() {
    assertEquals(listOf(12, 14, 18, 22), listOf(14, 16, 22, 28).map(MessageFontSizes::quoteSize))
  }

  @Test
  fun `all slider positions support quotes without snapping the message setting`() {
    val sizes = (14..42).map(MessageFontSizes::quoteSize)
    assertTrue(sizes.zipWithNext().all { (a, b) -> b >= a })
    assertEquals(31, MessageFontSizes.quoteSize(42))
    assertEquals(17, MessageFontSizes.quoteSize(20))
  }

  @Test
  fun `corrupt or old settings are bounded`() {
    assertEquals(12, MessageFontSizes.quoteSize(Int.MIN_VALUE))
    assertEquals(31, MessageFontSizes.quoteSize(Int.MAX_VALUE))
    assertEquals(42, MessageFontSizes.MAX)
  }
}
