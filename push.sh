set -euo pipefail
if ! command -v git >/dev/null 2>&1; then
    echo "git is not installed or not in PATH." >&2
    exit 1
fi
git config core.ignorecase false || true
echo "Updating local branch"
if ! git pull --ff-only; then
    git pull
fi
echo
read -r -p "Enter description of changes (leave empty to use timestamp): " msg
if [ -z "$msg" ]; then
    msg="Auto-commit: $(date -u +"%Y-%m-%dT%H:%M:%SZ")"
fi
echo
echo "Adding all files"
git add --all
echo
echo "Committing with message: $msg"
if git diff --cached --quiet; then
    echo "No changes to commit. Skipping commit."
else
    git commit -m "$msg"
fi
echo
echo "Pushing to repository"
git push
echo
echo "Synced"