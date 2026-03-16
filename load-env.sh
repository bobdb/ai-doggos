#!/usr/bin/env bash
# Usage: source ./load-env.sh
set -a
source "$(dirname "${BASH_SOURCE[0]}")/.env"
set +a
echo "Environment variables loaded from .env"
