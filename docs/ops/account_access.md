# Account and Secret Access (v1.0)

Do not store secrets here. Record who has custody and where secrets live.

- [ ] Google Play Console access confirmed (roles/owners)
- [ ] Play App Signing status documented
- [ ] Upload keystore custody recorded (location, owners)
- [ ] Analytics/telemetry credentials accounted for
- [ ] CI secret storage confirmed (GitHub Secrets/other vault)

## Access table
| Service | Role/permission | Owners | Storage location/reference | Notes |
| --- | --- | --- | --- | --- |
| Google Play Console | Admin/release manager | LonghornSneal (sole owner) | Play Console; access via SSO | Capture "App signing enabled" screenshot when ready |
| Play App Signing | Upload cert, Google-managed signing | LonghornSneal | Play Console → App signing | Pending enablement |
| Upload keystore | Signing/upload | LonghornSneal | Secure vault; base64 for GH secret | Generate after Play App Signing enabled |
| Analytics (e.g., Firebase) | Editor/service account | LonghornSneal (or confirm provider) | Secret manager; JSON not in repo | Confirm provider/owner |
| GitHub Secrets | Admin | LonghornSneal | GitHub repo settings | Needed: PLAY_SERVICE_ACCOUNT_JSON, UPLOAD_KEYSTORE_BASE64, UPLOAD_KEY_ALIAS, UPLOAD_KEY_PASSWORD, STORE_PASSWORD |

## Actions/requests
- Enable Play App Signing now; download/upload certificate as evidence and store securely.
- Generate upload keystore now; store securely; add base64 + passwords to GitHub Secrets (no secrets in repo).
- Create Play service account with upload rights now; store JSON in vault; add GH secret reference only.
- Confirm analytics provider and credentials handling.

## GitHub Secrets to create (names only; do not commit values)
- PLAY_SERVICE_ACCOUNT_JSON: contents of the Play service account JSON (base64-encode or store as plain JSON string).
- UPLOAD_KEYSTORE_BASE64: base64 of the upload keystore file.
- UPLOAD_KEY_ALIAS: alias used when creating the keystore.
- UPLOAD_KEY_PASSWORD: password for the key entry.
- STORE_PASSWORD: password for the keystore.

## Upload keystore generation (local placeholder steps)
1. After Play App Signing is enabled, generate the upload keystore locally (example):
   `keytool -genkeypair -v -keystore upload-keystore.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias UPLOAD_ALIAS`
2. Base64 the keystore for GitHub Secrets: `base64 upload-keystore.jks > upload-keystore.jks.b64`
3. Store the original `.jks` in a secure vault; do not commit it.

## Play service account (local placeholder steps)
1. In Play Console, create a service account with upload/release rights.
2. Download the JSON credentials; store in a secure vault (not in the repo).
3. Add the JSON content to PLAY_SERVICE_ACCOUNT_JSON GitHub Secret.
