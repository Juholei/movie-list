(ns movie-list.views
  (:require [re-frame.core :as re-frame]
            [movie-list.subs :as subs]
            [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [reagent.core :as r]))

(defn main-panel []
  [ui/mui-theme-provider
   {:mui-theme (get-mui-theme
                 {:palette {:text-color (color :green600)}})}
   [:div
    [ui/app-bar {:title "Title"
                 :icon-element-right
                        (r/as-element [ui/icon-button
                                       (ic/action-account-balance-wallet)])}]
    [ui/paper
     [:div "Hello"]]]])
