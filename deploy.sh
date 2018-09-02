#!/usr/bin/env bash
aws s3 sync --delete resources/public/ s3://$BUCKET/ --exclude 'js/compiled/out/*' --acl public-read
