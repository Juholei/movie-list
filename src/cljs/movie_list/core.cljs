(ns movie-list.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [movie-list.events :as events]
            [movie-list.views :as views]
            [movie-list.config :as config]
            [movie-list.router :as router]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (router/start-router!)
  (dev-setup)
  (mount-root))
