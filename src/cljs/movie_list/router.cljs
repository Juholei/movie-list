(ns movie-list.router
  (:require [bide.core :as bide]
            [re-frame.core :as re-frame]
            [movie-list.events :as events]))

(def router (bide/router [["/:list-name" ::main-route]]))

(defn list-url [name imdb-ids]
  (str (-> js/window
           .-location
           .-origin)
       "/#"
       (bide/resolve router ::main-route {:list-name name} imdb-ids)))

(defn on-navigate [name params query]
  (re-frame/dispatch [::events/change-list-name (:list-name params)]))

(defn start-router! []
  (bide/start! router {:default ::main-route
                       :on-navigate on-navigate}))
