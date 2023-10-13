#!/bin/sh

set -eu

ARGS=$1

output=$(svu $ARGS)

echo "::set-output name=output::${output}"
