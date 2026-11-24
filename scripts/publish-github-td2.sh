#!/usr/bin/env bash
# TD2 exercice 1 : dépôt GitHub privé + invitations + push
set -euo pipefail
cd "$(dirname "$0")/.."

GH="${GH:-/opt/homebrew/bin/gh}"

if ! "$GH" auth status &>/dev/null; then
  echo "Connecte-toi d'abord : gh auth login -h github.com -p https -w"
  exit 1
fi

REPO_NAME="${REPO_NAME:-SwapCampus_GeoffroyKevan}"

if git remote get-url origin &>/dev/null; then
  echo "Remote origin déjà configuré."
else
  "$GH" repo create "$REPO_NAME" --private --source=. --remote=origin --description="SwapCampus II.3510 — TD2 + lab KMP"
fi

git push -u origin main

for email in jerome.baton@ext.isep.fr maurras.togbe@isep.fr; do
  "$GH" api "repos/$("$GH" api user -q .login)/${REPO_NAME}/invitations" \
    -f email="$email" -f permission=push || echo "Invitation échouée pour $email (compte GitHub absent ?)"
done

echo "Dépôt : $("$GH" repo view --json url -q .url)"
