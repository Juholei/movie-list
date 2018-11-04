(ns movie-list.events
  (:require [re-frame.core :as re-frame]
            [movie-list.db :as db]
            [movie-list.config :refer [omdb-api-key]]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [camel-snake-kebab.core :refer [->kebab-case-keyword]]
            [camel-snake-kebab.extras :refer [transform-keys]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(defn set-dragged-item [db [_ item]]
  (if item
    (assoc db :dragged-item item)
    (dissoc db :dragged-item)))

(re-frame/reg-event-db
  ::set-dragged-item
  set-dragged-item)

(re-frame/reg-event-db
  ::clear-dragged-item
  (fn [db _]
    (set-dragged-item db nil)))

(re-frame/reg-event-db
  ::drag-finished
  (fn [{:keys [dragged-item] :as db} [_ destination]]
    (update db :list (fn [list]
                       (let [destination-index (.indexOf list destination)
                            [start end] (split-at destination-index (remove #{dragged-item} list))]
                         (concat start [dragged-item] end))))))

(defn set-add-movie-modal-open [db [_ open?]]
  (assoc db :dialog-open? open?))

(defn set-error
  ([db]
    (set-error db nil))
  ([db [_ error-msg]]
   (assoc db :error error-msg)))

(defn set-movie-name
  ([db]
    (set-movie-name db [nil ""]))
  ([db [_ name]]
    (assoc db :movie-name name)))

(defn show-add-movie-modal [db [_ open?]]
  (-> db
      (set-add-movie-modal-open [_ open?])
      set-error
      set-movie-name))

(re-frame/reg-event-db
  ::show-add-movie-modal
  show-add-movie-modal)

(re-frame/reg-event-db
  ::server-error
  (fn [db _]
    (assoc db :error "Problem connecting to OMDB. Please try again." :in-progress? false)))

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
               :movie-name nil
               :in-progress? false)
        (update :list sort-movies order))))

(defn update-progress-status [db in-progress?]
  (assoc db :in-progress? in-progress?))

(defn update-search-results [db results]
  (-> db
      (update :search-results (partial apply conj) (:search (transform-keys ->kebab-case-keyword results)))
      (update :search-results distinct)))

(defn set-search-results [db [_ results]]
  (-> db
      (update-search-results results)
      (update-progress-status false)))

(re-frame/reg-event-db
  ::set-search-results
  set-search-results)

(re-frame/reg-event-fx
  ::search-movie
  (fn [{:keys [db]} [_ name]]
    {:db         (assoc db :in-progress? true :error nil)
     :http-xhrio {:method          :get
                  :uri             (str "https://www.omdbapi.com/?apikey=" omdb-api-key "&type=movie&s=" name)
                  :timeout         8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::set-search-results]
                  :on-failure      [::server-error]}}))

(re-frame/reg-event-fx
  ::retrieve-movie-by-id
  (fn [_ [_ id]]
    {:http-xhrio {:method          :get
                  :uri             (str "https://www.omdbapi.com/?apikey=" omdb-api-key "&i=" id)
                  :timeout         8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::add-movie-to-list]
                  :on-failure      [::server-error]}}))

(re-frame/reg-event-fx
  ::retrieve-movie-by-name
  (fn [{:keys [db]} [_ id]]
    {:db         (assoc db :in-progress? true)
     :http-xhrio {:method          :get
                  :uri             (str "https://www.omdbapi.com/?apikey=" omdb-api-key "&t=" id)
                  :timeout         8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::add-movie-to-list]
                  :on-failure      [::server-error]}}))

(re-frame/reg-event-db
  ::set-movie-name
  set-movie-name)

(defn remove-dragged-item [{:keys [dragged-item] :as db} _]
  (-> db
      (update :list (partial remove #(= dragged-item %)))
      (dissoc :dragged-item)))

(re-frame/reg-event-db
  ::delete-dragged-movie
  remove-dragged-item)

(defn set-list-name [db [_ name]]
  (assoc db :list-name name))

(re-frame/reg-event-db
  ::change-list-name
  set-list-name)

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
