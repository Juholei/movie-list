(ns movie-list.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [movie-list.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
