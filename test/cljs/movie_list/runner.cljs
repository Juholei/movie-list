(ns movie-list.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [movie-list.test-events]))

(doo-tests 'movie-list.test-events)
