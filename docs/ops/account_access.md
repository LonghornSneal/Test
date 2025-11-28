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
| Google Play Console | Admin/release manager | LonghornSneal (sole owner) | Play Console; access via SSO | Capture “App signing enabled” screenshot when ready |
| Play App Signing | Upload cert, Google-managed signing | LonghornSneal | Play Console → App signing | Pending enablement |
| Upload keystore | Signing/upload | LonghornSneal | Secure vault; base64 for GH secret | Generate after Play App Signing enabled |
| Analytics (e.g., Firebase) | Editor/service account | LonghornSneal (or confirm provider) | Secret manager; JSON not in repo | Confirm provider/owner |
| GitHub Secrets | Admin | LonghornSneal | GitHub repo settings | Needed: PLAY_SERVICE_ACCOUNT_JSON, UPLOAD_KEYSTORE_BASE64, UPLOAD_KEY_ALIAS, UPLOAD_KEY_PASSWORD, STORE_PASSWORD |

## Actions/requests
- Confirm Play Console admins and enable Play App Signing; store upload cert.
- Generate upload keystore; store securely; add base64 + passwords to GitHub Secrets.
- Create Play service account with upload rights; store JSON in vault; add GH secret.
- Confirm analytics provider and credentials handling.
