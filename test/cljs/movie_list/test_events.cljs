(ns movie-list.test-events
  (:require [cljs.test :refer-macros [deftest testing is]]
            [movie-list.events :as events]))

(deftest list-name-test
  (testing "Setting list name sets the name"
    (is (= {:list-name "Bestest Movies!"}
           (events/set-list-name {} [nil "Bestest Movies!"])))))

(deftest add-movie-modal-test
  (testing "Setting set-add-movie-modal-open to true"
    (is (= {:dialog-open? true}
           (events/set-add-movie-modal-open {} [nil true]))))
  (testing "show-add-movie-modal sets the modal without errors or movie name"
    (is (= {:dialog-open? true
            :error nil
            :movie-name ""}
           (events/show-add-movie-modal {} [nil true])))))

(deftest error-message-test
  (testing "Error message is set to kaikki meni :("
    (is (= {:error "Kaikki meni :("}
           (events/set-error {} [nil "Kaikki meni :("])))))

(deftest movie-name-test
  (testing "Setting movie name"
    (is (= {:movie-name "Kovaa peliä Ranualla"}
           (events/set-movie-name {} [nil "Kovaa peliä Ranualla"])))))
