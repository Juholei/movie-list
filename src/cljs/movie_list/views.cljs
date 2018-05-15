(ns movie-list.views
  (:require [re-frame.core :as re-frame]
            [movie-list.subs :as subs]
            [movie-list.events :as events]
            [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]))

(defn movie [movie-data]
  [ui/card {:draggable     true
            :on-drag-over  #(.preventDefault %)
            :on-drag-start #(do (.setData (.-dataTransfer %) "text/plain" " ")
                                (re-frame/dispatch [::events/set-dragged-item movie-data]))
            :on-drag-end   #(re-frame/dispatch [::events/clear-dragged-item])
            :on-drop       #(re-frame/dispatch [::events/drag-finished movie-data])}
   [ui/card-header {:title                  (:title movie-data)
                    :subtitle               (:year movie-data)
                    :avatar                 (:poster movie-data)
                    :act-as-expander        true
                    :show-expandable-button true}]
   [ui/card-text {:expandable true} (:plot movie-data)]])

(defn add-movie-dialog [open?]
  [ui/dialog {:title "Add a movie"
              :modal false
              :open open?}
   [:h1 "Hello it's me"]])
(defn main-panel []
  (let [list (re-frame/subscribe [::subs/list])
        list-name (re-frame/subscribe [::subs/list-name])
        open-dialog? (re-frame/subscribe [::subs/dialog-open?])]
    [ui/mui-theme-provider
     {:mui-theme (get-mui-theme
                   {:palette {:text-color (color :green600)}})}
     [:div
      [ui/app-bar {:title "Movie List Maker Pro"}]
      [add-movie-dialog @open-dialog?]
      [ui/paper
       [ui/list {:style {:width "30em"}}
        [ui/subheader @list-name]
        (for [movie-data @list]
          ^{:key (:imdb-id movie-data)}
          [movie movie-data])]
       [ui/floating-action-button {:on-click #(re-frame/dispatch [::events/open-add-movie-modal])}
        (ic/content-add {:color "white"})]]]]))
