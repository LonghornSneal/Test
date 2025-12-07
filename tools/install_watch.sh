#!/usr/bin/env bash
set -euo pipefail

# Installs the CosmoBond watch face APK onto a Wear OS watch over Wi‑Fi.
# Usage: ./tools/install_watch.sh <WATCH_IP:PORT> [APK_PATH]
# Example: ./tools/install_watch.sh 10.195.4.92:36965 app/build/outputs/apk/debug/app-debug.apk

TARGET="${1:-}"
APK_PATH="${2:-app/build/outputs/apk/debug/app-debug.apk}"
PACKAGE_NAME="com.cosmobond.watchface"
SERVICE_NAME="${PACKAGE_NAME}/.CosmoBondWatchFaceService"

if [[ -z "${TARGET}" ]]; then
  echo "Usage: $0 <WATCH_IP:PORT> [APK_PATH]" >&2
  exit 1
fi

if [[ ! -f "${APK_PATH}" ]]; then
  echo "APK not found at ${APK_PATH}. Build the APK first." >&2
  exit 1
fi

echo "Connecting to ${TARGET}..."
adb connect "${TARGET}" >/dev/null

echo "Installing ${APK_PATH}..."
adb -s "${TARGET}" install -r "${APK_PATH}"

echo "Setting CosmoBond as the active watch face..."
adb -s "${TARGET}" shell cmd watchface set "${SERVICE_NAME}"

echo "Done. If the face does not appear immediately, wake the watch and swipe to the CosmoBond watch face."
