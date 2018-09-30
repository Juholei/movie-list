(ns movie-list.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [movie-list.test-events]
              [movie-list.test-subs]))

(doo-tests 'movie-list.test-events
           'movie-list.test-subs)
