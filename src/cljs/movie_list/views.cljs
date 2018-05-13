(ns movie-list.views
  (:require [re-frame.core :as re-frame]
            [movie-list.subs :as subs]
            [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]))

(defn movie [movie-data]
  [ui/card {:draggable true
            :style     {:width "30em"}}
   [ui/card-header {:title                  (:title movie-data)
                    :subtitle               (:year movie-data)
                    :avatar                 (:poster movie-data)
                    :act-as-expander        true
                    :show-expandable-button true}]
   [ui/card-text {:expandable true} (:plot movie-data)]])

(defn main-panel []
  (let [lists (re-frame/subscribe [::subs/lists])]
    [ui/mui-theme-provider
     {:mui-theme (get-mui-theme
                   {:palette {:text-color (color :green600)}})}
     [:div
      [ui/app-bar {:title "Movie List Maker Pro"}]
      [ui/paper
       [:div [:h1 (:name (first @lists))]]
       [ui/list
        (for [movie-data (:contents (first @lists))]
          ^{:key (:imdb-id movie-data)}
          [movie movie-data])]
       [ui/floating-action-button (ic/content-add {:color "white"})]]]]))
