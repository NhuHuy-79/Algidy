---
name: kta-design-tokens
description: Initialize or update Jetpack Compose design tokens for the CURRENT project — no external design source needed. Auto-discovers the project's theme module, token prefix, layer architecture, and conventions, then creates missing tokens or updates existing ones. If invoked without a request, interviews the user (mode, style direction, hardcoded-value migration) via AskUserQuestion and executes. Harvest mode derives the token scale from a messy codebase (inventory → cluster → canonicalize → tracked migration). Use when user asks to "init design tokens", "update design tokens", "add token", "restyle theme", "fix hardcoded colors/spacing", "tokenize/harvest this messy Compose code", or invokes the skill bare. For Figma/Stitch/Claude-design sources, route to kta-compose-design-tokens instead.
---

# KTA Design Tokens

Initialize or update Compose design tokens **from the current project itself**. This skill owns the
project's token lifecycle; it never requires an external design source.

## Routing Gate (RUN FIRST)

- If the request references an external design source (Figma URL, Stitch, Claude design spec,
  `tokens.json`), STOP and invoke `kta-compose-design-tokens` instead.
- Otherwise proceed.

## Invocation Contract

| Invocation                                                               | Behavior                                                                          |
|--------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| Skill + request (e.g. "add motion tokens", "darken the primary palette") | Skip interview. Run Discovery, then execute the request directly.                 |
| Skill with no request                                                    | Run Discovery, then Interview (below), then execute without further confirmation. |

## Phase 1: Discovery (ALWAYS, before anything else)

Load `references/discovery-workflow.md`. Never hardcode module names, prefixes, or dependency
versions — discover everything:

1. **Theme module**: find the Gradle module holding theme/token code (search for `MaterialTheme`,
   `CompositionLocalProvider`, `*Theme.kt`, `*Tokens.kt`). Record its Gradle path (e.g. `:theme`).
2. **Conventions**: token file naming, class prefix (e.g. `Qzds*`), package, `@Immutable`/`@Stable`
   usage, `staticCompositionLocalOf` pattern, light/dark variants, M3 bridge.
3. **Architecture**: detect layer structure (Primitive → Semantic → Component, or flat). Follow what
   exists; only propose a structure when the project has none.
4. **Versions**: read `gradle/libs.versions.toml` / build files for Kotlin, Compose BOM, minSdk.
   Generated code must compile against these.
5. **Coverage map**: list existing token categories (color, typography, spacing, shape, elevation,
   opacity, motion, component tokens) and gaps.

Output a short discovery summary (module, prefix, layers, categories present/missing) before
executing.

## Phase 2: Interview (only when invoked with NO request)

Use the AskUserQuestion tool — one call, then execute. Questions:

1. **Mode** — `Init` (no tokens, style chosen by user → scaffold full set), `Harvest` (
   no/fragmentary tokens but lots of scattered raw values → derive the scale FROM the codebase, then
   migrate onto it), or `Update` (tokens exist → fill gaps, fix inconsistencies). Pre-select from
   discovery: tokens exist → Update; no tokens + many raw values in UI code → Harvest; no tokens +
   little existing UI → Init.
2. **Style direction** — ask only when Mode = Init, or Mode = Update and user picked a restyle
   option: primary palette family, light/dark support, mood (playful / neutral / premium). Offer "
   keep current style" when tokens exist. Never ask for Harvest — style comes from the codebase.
3. **Hardcoded-value migration** — scan consuming modules for hardcoded `Color(0x...)`, raw `.dp`/
   `.sp`, inline `TextStyle`, and replace with token references? (Yes: full scan / Yes: theme +
   ui-components only / No).

Do NOT ask about token categories — infer needed categories from the discovery coverage map and the
chosen mode.

## Phase 3: Execute

Load `references/generation-rules.md`.

- **Init**: scaffold the discovered (or agreed) layer structure — primitives first, semantic mapping
  second, theme provider + CompositionLocals last. Match project code style exactly (naming,
  `@Immutable`, file-per-category).
- **Harvest**: load `references/harvest-clustering.md`. Inventory every raw design value in the
  codebase → cluster near-duplicates → canonicalize into a scale (show the derived scale to the
  user, one checkpoint) → generate tokens → tracked per-module migration with resume via
  `plans/{date}-token-harvest/migration-todo.md`.
- **Update**: smallest diff that closes the gap. Extend existing files; create new files only for
  genuinely new categories. Never rename or restructure existing public tokens unless the request
  says so.
- **Migration** (if chosen): load `references/hardcoded-migration.md`. Replace hardcoded values with
  the nearest existing token; create a token only when no reasonable match exists. Report every
  replacement.

## Phase 4: Verify (MANDATORY)

1. Compile the theme module: `./gradlew {themeModule}:compileDebugKotlin` (use the discovered Gradle
   path).
2. If migration touched other modules, compile those too (Harvest mode compiles incrementally per
   module during migration).
3. Fix compile errors and re-run until green. Never leave the build broken.
4. Harvest mode: snapped values change pixels — run screenshot/preview tests if the project has
   them; otherwise include the snap register in the report and flag it for a manual visual pass.

## Report

End with: discovery summary, files created/updated, migration replacements count (if any), snap
register (Harvest), compile result.
