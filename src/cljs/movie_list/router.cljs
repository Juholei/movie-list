(ns movie-list.router
  (:require [bide.core :as bide]
            [re-frame.core :as re-frame]
            [movie-list.events :as events]))

(def router (bide/router [["/:list-name" ::main-route]]))

(defn list-url [name imdb-ids]
  (str (-> js/window
           .-location
           .-origin)
       (-> js/window
           .-location
           .-pathname)
       "#"
       (bide/resolve router ::main-route {:list-name name} imdb-ids)))

(defn movie-to-position [acc [order-keyword movie-id]]
  (assoc acc movie-id (int (name order-keyword))))

(defn query-params->list-order-map [query]
  (reduce movie-to-position {} query))

(defn on-navigate [_ params query]
  (let [order-of-movies (query-params->list-order-map query)
        list-name (some-> params
                          :list-name
                          js/decodeURIComponent)]
    (when list-name
      (re-frame/dispatch [::events/change-list-name list-name]))
    (re-frame/dispatch [::events/set-order-list order-of-movies])
    (doseq [id (vals query)]
      (re-frame/dispatch [::events/retrieve-movie-by-id id]))))

(defn start-router! []
  (bide/start! router {:default ::main-route
                       :on-navigate on-navigate}))
