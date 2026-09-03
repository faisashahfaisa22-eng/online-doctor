# Security Policy

## Reporting a vulnerability
Please do not open public GitHub issues for vulnerabilities that expose secrets,
private health data, authentication bypasses, or remote-code-execution paths.
Use GitHub's private vulnerability reporting feature when enabled, or contact the
repository owner privately.

## Secrets
- Never place `OPENAI_API_KEY` in the Android app, browser JavaScript, commits, screenshots, or GitHub Actions logs.
- Keep secrets only on the backend/hosting platform.
- Rotate any key that was ever committed publicly.

## Health data
This starter stores profile/history locally in the client. Before production,
implement an explicit privacy model, encryption where appropriate, retention and
deletion controls, audit logging, and applicable legal/regulatory requirements.
