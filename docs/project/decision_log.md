# Decision Log

## 2025-11-26 — Task 1: Guardrail audit
- Reviewed guardrail files (`README.md`, `CODEOWNERS`, `CONTRIBUTING.md`, `LICENSE`, `.gitignore`, `.editorconfig`, `.gitattributes`).
- Findings / follow-ups:
  - `README.md`: Add local run commands (lint/tests/build) and link to the active checklist; consider adding CI status badge once workflows settle.
  - `CODEOWNERS`: Single default owner `@cosmobond/watchface-maintainers`; verify team exists and add path-specific owners if different maintainers cover docs/art assets.
  - `CONTRIBUTING.md`: Lacks explicit command block for required checks (spotless/detekt/lint/tests) and no reference to code of conduct or PR templates—expand to reduce ambiguity.
  - `.gitignore`: Core entries present; consider adding `*.aab`, `app/src/main/baseline-prof.txt`, and secret/env files to avoid accidental commits.
  - `.gitattributes`: Treats common image formats as binary; add media types used in this repo (e.g., `*.mp4`) to prevent line-ending normalization.
- Branch protection: Unable to retrieve `main` protection via GitHub API without credentials (`curl .../branches/main/protection` returned 401). Request an admin to confirm settings in GitHub → Settings → Branches → Branch protection rules for `main` and enable required status checks (spotlessCheck, detekt, lint, testDebugUnitTest, connectedDebugAndroidTest), PR review requirements, and branch up-to-date enforcement; capture a screenshot when available.
- Markdown lint: `npx markdownlint-cli "**/*.md"` (2025-11-26) reported existing formatting/line-length violations across the repo (no files auto-fixed; exit code non-zero); no scope changes applied in this task.
