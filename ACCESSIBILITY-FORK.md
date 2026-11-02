# Accessibility controls (custom fork)

This branch is a UI-only customization of Signal Android. It is not an official
Signal release. The original AGPL-3.0 license and notices remain in place.

## Controls

- **Settings → Appearance → Message text size**: continuously draggable slider,
  persisted to the nearest whole sp, from 14 through 42 sp. Upstream maximum was
  28 sp. Existing values remain unchanged; quotes scale without snapping the
  message preference back to an old preset.
- **Interface text size**: independent 100–150% multiplier for menus and the
  conversation list. This multiplies Android's system font setting, not display
  size or dp geometry. Message bodies, composer, quotes and poll content retain
  system font scaling without this extra interface multiplier. Changes recreate
  the current Activity after release. Some auxiliary media/avatar activities
  outside the main Activity hierarchy retain system-only text sizing.
- **Chat color → Custom**: Brightness adjusts HSL lightness, alongside the
  existing hue and saturation controls, independently for both gradient stops.
  Existing custom colors restore all three components when edited. Save/Cancel
  retain their existing behavior.
- **Chat color → Use black outgoing text (all chats)**: local global preference;
  off preserves upstream white outgoing text. Affects outgoing body/link,
  footer/icon and poll content. Incoming messages, separately themed quote cards
  and media overlays keep their existing colors. Pick text and bubble brightness
  together; manual black/white selection cannot guarantee every color combination
  has sufficient contrast. This choice is not sent to conversation partners.

## Verification status

Source base: `signalapp/Signal-Android` main at
`515db93fb48206d63406cbfb48c5816f6af1d9e2`.

Passed locally: compiled the actual `MessageFontSizes.kt` with Kotlin 2.3.21 and
ran assertions for legacy quote values, intermediate sizes, monotonicity,
clamping and 42-sp maximum; parsed changed XML; `git diff --check`.

Added/updated automated tests for message and interface slider semantics,
maximum-size layout, ViewModel persistence/independence, quote sizing, system
versus interface font conversion, and brightness round trips/bounds/gradients.
These Android/Robolectric tests have **not run successfully in this environment**.
Gradle 9.6.0 was downloaded and started, but project configuration failed before
compilation while resolving Kotlin JVM plugin 2.2.20: Java networking reported
`Network is unreachable` through the configured proxy. The execution environment
also has no Android SDK installed. This is not a verified APK or full build pass.

Build prerequisites are the upstream Java21 toolchain, Android SDK37,
build-tools36.0.0 and NDK28.0.13004108 (see version catalog and reproducible-builds).
Run the upstream build workflow and focused tests before making any APK:

```
./gradlew :feature:app-settings:testDebugUnitTest
```

Also run the app's appropriate variant unit tests for
`AppearanceSettingsViewModelTest`, `AccessibilityFontUtilTest`, and
`ColorSlidersStateTest`, plus app lint and an unsigned assemble. Device QA still
needs TalkBack slider adjustments, Back/cancel/reopen, large system fonts,
light/dark themes, folded/unfolded display changes, outgoing/incoming text,
quotes, media, links, polls, and conversation return/rebinding.

## Safety / maintenance

No notification, FCM, message protocol, encryption, registration, migration,
signing identity or package ID changes are part of this patch. No app was
installed or account migrated. A self-signed build is not an update for the
official app. Do not replace a daily installation until signing, backup/restore,
message alerts and incoming-call behavior have been separately validated.
Maintain the fork against upstream security updates. Do not upload signing keys
or backup material into this repository.
