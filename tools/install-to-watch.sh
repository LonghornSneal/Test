#!/usr/bin/env bash
set -euo pipefail

usage() {
  echo "Usage: $(basename "$0") <watch_ip:port> <apk_path>" >&2
  echo "Example: $(basename "$0") 10.195.4.92:36965 ~/Downloads/cosmobond-build/app-debug.apk" >&2
}

if [[ $# -ne 2 ]]; then
  usage
  exit 1
fi

WATCH_ENDPOINT="$1"
APK_PATH="$2"

if [[ ! -f "$APK_PATH" ]]; then
  echo "APK not found at $APK_PATH" >&2
  exit 2
fi

if [[ "${APK_PATH##*.}" != "apk" ]]; then
  echo "Target file must be an .apk" >&2
  exit 3
fi

command -v adb >/dev/null 2>&1 || {
  echo "adb is required. Install android-tools (Termux) or Android Platform Tools." >&2
  exit 4
}

set -x
adb connect "$WATCH_ENDPOINT"
adb -s "$WATCH_ENDPOINT" install -r "$APK_PATH"
adb -s "$WATCH_ENDPOINT" shell cmd package list packages | head -n 5
