# FinSMS

On-device Android app that reads bank/UPI SMS, parses transactions locally, categorizes
spending, and shows analytics dashboards. No data ever leaves the device — no backend,
no network calls for parsing.

## Before you open this in Android Studio

1. **Gradle wrapper jar is not included.** This project was generated in an offline
   environment with no access to Gradle's distribution servers, so `gradle/wrapper/gradle-wrapper.jar`
   (the binary) is missing — only `gradle-wrapper.properties` is present. When you open the
   project, Android Studio will detect this and offer to regenerate the wrapper automatically
   (or prompt you to use its bundled Gradle version instead). Accept that prompt.
2. **Namespace/applicationId** is set to `com.kardev.finsms` throughout — matching what you
   already had. Change it in each module's `build.gradle.kts` if needed.
3. **Versions in `gradle/libs.versions.toml`** are reasonable current picks (AGP 8.6.0, Kotlin
   2.0.21, Compose BOM 2024.09.02, Hilt 2.51.1, Room 2.6.1) but were not compiled/verified in
   this environment (no Android SDK / network access to Google's Maven here). Android Studio
   will likely suggest a few version bumps on first sync — accept the AGP/Kotlin ones it
   recommends if prompted.
4. **This has not been compiled.** Treat it as a structurally complete, logically wired
   scaffold reflecting everything we designed in our conversation — not a verified working
   build. Expect to fix a handful of small issues on first sync (an import path, a version
   mismatch) rather than nothing at all.

## What's implemented

- Full multi-module structure: `app`, `core:common`, `core:database`, `core:designsystem`,
  `feature:parser`, `feature:sms-ingest`, `feature:transactions`, `feature:analytics`,
  `feature:instruments`
- Room database with all 5 entities, DAOs, and Hilt-provided `AppDatabase`
- Regex-based SMS parser for SBI/HDFC/ICICI/PNB (debit + credit) — **built from public,
  anonymized example formats, not your real SMS**. Validate against real messages first.
- Full ingestion pipeline: `BroadcastReceiver` (real-time) + `WorkManager` backfill worker
  (historical scan) → dedupe → categorize → store
- Rule-based categorization engine with a learning layer for user corrections
- Compose UI: Home, Transactions (searchable/filterable), Analytics, Review Queue
- SMS permission onboarding flow, foreground service to survive Doze

## What's NOT implemented yet

- Vico chart wiring on the Home trend view (dependency is included; the actual chart
  composable was left out of this pass — happy to add it)
- Instrument editing (rename/merge cards) — `feature:instruments` only lists them read-only
- Manual transaction entry (cash, etc.)
- Any tests

## First build steps

1. Open the project in Android Studio, let it sync (accept version-bump prompts if offered).
2. Run on a **real Android device**, not the emulator — SMS testing is unreliable on emulators.
3. Grant SMS permission when prompted; it'll trigger the backfill scan automatically.
4. Send yourself (or find in your inbox) a real bank/UPI SMS and check the Review Queue —
   if it lands in "Unrecognized SMS," that confirms the regex templates need tuning against
   your actual bank's format. Share the (redacted) SMS text and the templates can be fixed.
