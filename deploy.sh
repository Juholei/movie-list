#!/usr/bin/env bash
sed -i -e "s/(def omdb-api-key nil)/(def omdb-api-key \"$OMDBKEY\")/g" src/cljs/movie_list/config.cljs
lein clean && lein cljsbuild once min && aws s3 sync --delete resources/public/ s3://$BUCKET/ --exclude 'js/compiled/out/*' --acl public-read
