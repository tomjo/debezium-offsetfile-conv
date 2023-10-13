#!/bin/sh

set -eu

ARGS=$1

output=$(sudo svu $ARGS)

echo "::set-output name=output::${output}"
