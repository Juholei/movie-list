(ns movie-list.events
  (:require [re-frame.core :as re-frame]
            [movie-list.db :as db]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [camel-snake-kebab.core :refer [->kebab-case-keyword]]
            [camel-snake-kebab.extras :refer [transform-keys]]))

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
    (assoc db :dialog-open? open?)))

(re-frame/reg-event-db
  ::movie-not-found
  (fn [db _]
    (assoc db :error "Movie not found. I'm sorry.")))

(re-frame/reg-event-db
  ::add-movie-to-list
  (fn [db [_ movie]]
    (-> db
        (update :list conj (transform-keys ->kebab-case-keyword movie))
        (assoc :dialog-open? false
               :movie-name ""))))

(re-frame/reg-event-fx
  ::search-movie
  (fn [{:keys [db]} [_ name]]
    {:db         (assoc db :in-progress? true)
     :http-xhrio {:method          :get
                  :uri             (str "http://www.omdbapi.com/?apikey=[apikeyhere]&t=" name)
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