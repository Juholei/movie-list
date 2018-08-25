#!/usr/bin/env bash

lein clean && lein cljsbuild once min && aws s3 sync --delete resources/public/ s3://$BUCKET/ --exclude 'js/compiled/out/*' --acl public-read
