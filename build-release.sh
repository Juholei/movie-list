#!/usr/bin/env bash
sed -i -e "s/(def omdb-api-key nil)/(def omdb-api-key \"$OMDBKEY\")/g" src/cljs/movie_list/config.cljs
lein build-release
