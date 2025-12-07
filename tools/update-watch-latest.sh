#!/usr/bin/env bash
set -euo pipefail

usage() {
  echo "Usage: $(basename "$0") <watch_ip:port> [debug|release]" >&2
  echo "Example: $(basename "$0") 10.195.4.92:36965 debug" >&2
  echo "Requires: GH_TOKEN, GitHub CLI, adb on the phone." >&2
}

if [[ $# -lt 1 || $# -gt 2 ]]; then
  usage
  exit 1
fi

WATCH_ENDPOINT="$1"
VARIANT="${2:-debug}"

if [[ "$VARIANT" != "debug" && "$VARIANT" != "release" ]]; then
  echo "Variant must be 'debug' or 'release'." >&2
  exit 2
fi

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
  exit 3
fi

if ! command -v gh >/dev/null 2>&1; then
  echo "Install GitHub CLI (gh) and authenticate with a token that can read workflow runs." >&2
  exit 4
fi

command -v adb >/dev/null 2>&1 || {
  echo "adb is required. Install android-tools (Termux) or Android Platform Tools." >&2
  exit 5
}

WORKFLOW_NAME="Build installable artifacts"
ARTIFACT_NAME="cosmobond-${VARIANT}-apk"
EXPECTED_APK="app-${VARIANT}.apk"

echo "Finding latest successful '$WORKFLOW_NAME' run in $REPO..."
RUN_ID=$(gh run list \
  -R "$REPO" \
  -w "$WORKFLOW_NAME" \
  -s success \
  -L 1 \
  --json databaseId \
  --jq '.[0].databaseId')

if [[ -z "$RUN_ID" || "$RUN_ID" == "null" ]]; then
  echo "No successful runs found. Trigger the workflow from GitHub Actions first." >&2
  exit 6
fi

TEMP_DIR=$(mktemp -d)
cleanup() {
  rm -rf "$TEMP_DIR"
}
trap cleanup EXIT

echo "Downloading artifact '$ARTIFACT_NAME'..."
if ! gh run download "$RUN_ID" -R "$REPO" -n "$ARTIFACT_NAME" -D "$TEMP_DIR"; then
  echo "Artifact '$ARTIFACT_NAME' not found in the latest run." >&2
  exit 7
fi

APK_PATH=$(find "$TEMP_DIR" -name "$EXPECTED_APK" -type f | head -n 1)

if [[ -z "$APK_PATH" ]]; then
  echo "APK not located after download; expected $EXPECTED_APK." >&2
  exit 8
fi

echo "Installing $APK_PATH to $WATCH_ENDPOINT..."
set -x
adb connect "$WATCH_ENDPOINT"
adb -s "$WATCH_ENDPOINT" install -r "$APK_PATH"
