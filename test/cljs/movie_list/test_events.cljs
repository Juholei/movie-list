(ns movie-list.test-events
  (:require [cljs.test :refer-macros [deftest testing is]]
            [movie-list.events :as events]))

(deftest list-name-test
  (testing "Setting list name sets the name"
    (is (= {:list-name "Bestest Movies!"}
           (events/set-list-name {} [nil "Bestest Movies!"])))))
