(ns movie-list.router
  (:require [bide.core :as bide]))

(def router (bide/router [["/:list-name" ::main-route]]))

(defn list-url [name imdb-ids]
  (str (-> js/window
           .-location
           .-origin)
       "/#"
       (bide/resolve router ::main-route {:list-name name} imdb-ids)))