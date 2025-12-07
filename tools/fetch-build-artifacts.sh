#!/usr/bin/env bash
set -euo pipefail

WORKFLOW_NAME="Build installable artifacts"
DEST_DIR="${1:-artifacts}"
REPO="${GH_REPO:-}"

if [[ -z "$REPO" ]]; then
  if git_remote_url=$(git config --get remote.origin.url 2>/dev/null) && [[ -n "$git_remote_url" ]]; then
    if [[ "$git_remote_url" =~ github.com[:/]+([^/]+/[^/.]+) ]]; then
      REPO="${BASH_REMATCH[1]}"
    fi
  fi
fi

if [[ -z "$REPO" ]]; then
  echo "Set GH_REPO=owner/repo or configure a remote origin so we can locate the workflow." >&2
  exit 1
fi

if ! command -v gh >/dev/null 2>&1; then
  echo "Install GitHub CLI (gh) and authenticate with a token that can read workflow runs." >&2
  exit 1
fi

echo "Looking for the latest successful '$WORKFLOW_NAME' run in $REPO..."
RUN_ID=$(gh run list \
  -R "$REPO" \
  -w "$WORKFLOW_NAME" \
  -s success \
  -L 1 \
  --json databaseId,displayTitle,headBranch,updatedAt \
  --jq '.[0].databaseId')

if [[ -z "$RUN_ID" || "$RUN_ID" == "null" ]]; then
  echo "No successful runs found for '$WORKFLOW_NAME'. Trigger it from GitHub Actions first." >&2
  exit 1
fi

echo "Latest run ID: $RUN_ID"
mkdir -p "$DEST_DIR"

for ARTIFACT in cosmobond-debug-apk cosmobond-release-apk cosmobond-release-aab; do
  echo "Downloading artifact '$ARTIFACT' into $DEST_DIR..."
  if ! gh run download "$RUN_ID" -R "$REPO" -n "$ARTIFACT" -D "$DEST_DIR"; then
    echo "Skipped '$ARTIFACT' (not published in this run)." >&2
  fi
done

echo "Done. Check $DEST_DIR for APK/AAB files ready to sideload or upload."
