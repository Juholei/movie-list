(ns movie-list.events
  (:require [re-frame.core :as re-frame]
            [movie-list.db :as db]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [camel-snake-kebab.core :refer [->kebab-case-keyword]]
            [camel-snake-kebab.extras :refer [transform-keys]]))

(def omdb-api-key nil)

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
  ::set-dragged-item
  (fn [db [_ item]]
    (assoc db :dragged-item item)))

(re-frame/reg-event-db
  ::clear-dragged-item
  (fn [db _]
    (dissoc db :dragged-item)))

(re-frame/reg-event-db
  ::drag-finished
  (fn [{:keys [dragged-item] :as db} [_ destination]]
    (update db :list (fn [list]
                       (let [destination-index (.indexOf list destination)
                            [start end] (split-at destination-index (remove #{dragged-item} list))]
                         (concat start [dragged-item] end))))))

(re-frame/reg-event-db
  ::set-add-movie-modal-open
  (fn [db [_ open?]]
    (assoc db :dialog-open? open? :error nil :movie-name "")))

(re-frame/reg-event-db
  ::movie-not-found
  (fn [db _]
    (assoc db :error "Movie not found. I'm sorry." :in-progress? false)))

(defn sort-movies [list order]
  (if order
    (sort-by #(order (:imdb-id %)) list)
    list))

(re-frame/reg-event-db
  ::add-movie-to-list
  (fn [{:keys [order] :as db} [_ movie]]
    (-> db
        (update :list conj (transform-keys ->kebab-case-keyword movie))
        (assoc :dialog-open? false
               :movie-name "")
        (update :list sort-movies order))))

(re-frame/reg-event-db
  ::set-search-results
  (fn [db [_ results]]
    (-> db
        (update :search-results (partial apply conj) (:search (transform-keys ->kebab-case-keyword results)))
        (update :search-results distinct)
        (assoc :in-progress? false))))

(re-frame/reg-event-fx
  ::search-movie
  (fn [{:keys [db]} [_ name]]
    {:db         (assoc db :in-progress? true :error nil)
     :http-xhrio {:method          :get
                  :uri             (str "http://www.omdbapi.com/?apikey=" omdb-api-key "&type=movie&s=" name)
                  :timeout         8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::set-search-results]
                  :on-failure      [::movie-not-found]}}))

(re-frame/reg-event-fx
  ::retrieve-movie-by-id
  (fn [_ [_ id]]
    {:http-xhrio {:method          :get
                  :uri             (str "http://www.omdbapi.com/?apikey=" omdb-api-key "&i=" id)
                  :timeout         8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::add-movie-to-list]
                  :on-failure      [::movie-not-found]}}))

(re-frame/reg-event-fx
  ::retrieve-movie-by-name
  (fn [{:keys [db]} [_ id]]
    {:db         (assoc db :in-progress? true)
     :http-xhrio {:method          :get
                  :uri             (str "http://www.omdbapi.com/?apikey=" omdb-api-key "&t=" id)
                  :timeout         8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::add-movie-to-list]
                  :on-failure      [::movie-not-found]}}))

(re-frame/reg-event-db
  ::set-movie-name
  (fn [db [_ name]]
    (assoc db :movie-name name)))

(re-frame/reg-event-db
  ::delete-dragged-movie
  (fn [{:keys [dragged-item] :as db} _]
    (update db :list (partial remove #(= dragged-item %)))))

(re-frame/reg-event-db
  ::change-list-name
  (fn [db [_ name]]
    (assoc db :list-name name)))

(re-frame/reg-event-db
  ::set-order-list
  (fn [db [_ order-of-movies]]
    (assoc db :order order-of-movies)))

(re-frame/reg-event-db
  ::set-share-list-dialog-open
  (fn [db [_ open?]]
    (assoc db :share-list-dialog-open? open?)))

(re-frame/reg-event-db
  ::set-alert-message
  (fn [db [_ message]]
    (assoc db :alert-message message)))
