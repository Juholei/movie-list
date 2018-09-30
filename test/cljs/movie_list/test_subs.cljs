(ns movie-list.test-subs
  (:require [cljs.test :refer-macros [deftest testing is]]
            [movie-list.subs :as subs]))

(deftest list-name-sub
  (testing "List name subscription returns correct name"
    (is (= "Bestest movies"
           (subs/list-name {:list-name "Bestest movies"})))))
