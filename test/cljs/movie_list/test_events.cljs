(ns movie-list.test-events
  (:require [cljs.test :refer-macros [deftest testing is]]
            [movie-list.events :as events]
            [clojure.string :as string]))

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

(deftest search-results-test
  (testing "Setting search results results movie being added
            to the results and progress status being updated"
    (is (= {:search-results [{:title "Hot Fuzz"}]
            :in-progress? false}
           (events/set-search-results {} [nil {"search" [{"title" "Hot Fuzz"}]}]))))
  (testing "Updating search results adds the movie to the results"
    (is (= {:search-results [{:title "Hot Fuzz"}]}
           (events/update-search-results {} {"search" [{"title" "Hot Fuzz"}]})))))

(deftest dragged-item-test
  (testing "Given movie is removed from the movie list"
    (is (= {:list [{:title "Hot Fuzz"} {:title "Scott Pilgrim vs. the World"}
                   {:title "Baby Driver"}]}
           (events/remove-dragged-item {:list         [{:title "Hot Fuzz"}
                                                       {:title "Scott Pilgrim vs. the World"}
                                                       {:title "Baby Driver"}
                                                       {:title "A Bad Movie"}]
                                        :dragged-item {:title "A Bad Movie"}} nil))))
  (testing "Dragged item can be set"
    (is (= {:dragged-item {:title "Hot Fuzz"}}
           (events/set-dragged-item {} [nil {:title "Hot Fuzz"}]))))
  (testing "Setting dragged item with nil clears the item"
    (is (= {}
           (events/set-dragged-item {} [nil nil])))))

(deftest http-fx-test
  (testing "retrieve-movie-by-name constructs correct url to retrieve movie data "
    (let [{:keys [http-xhrio]} (events/retrieve-movie-by-name {:db {}} [nil "Hot Fuzz"])]
      (is (string/starts-with? (:uri http-xhrio)
                               "https://www.omdbapi.com/?apikey="))
      (is (string/ends-with? (:uri http-xhrio)
                             "&t=Hot Fuzz"))))
  (testing "retrieve-movie-by-name sets progress to true"
    (is (= {:in-progress? true}
           (:db (events/retrieve-movie-by-name {:db {}} [nil "Hot Fuzz"]))))))
