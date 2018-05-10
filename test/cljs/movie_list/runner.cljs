(ns movie-list.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [movie-list.core-test]))

(doo-tests 'movie-list.core-test)
