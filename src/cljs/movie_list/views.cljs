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
  (let [movie-name (re-frame/subscribe [::subs/movie-name])]
    [ui/dialog {:title "Add a movie"
                :modal false
                :open  open?
                :on-request-close #(re-frame/dispatch [::events/set-add-movie-modal-open false])}
     [ui/text-field {:hint-text "Type name of the movie"
                     :value     @movie-name
                     :on-change #(re-frame/dispatch [::events/set-movie-name (-> %
                                                                                 .-target
                                                                                 .-value)])}]
     [ui/raised-button {:label "Add movie"
                        :label-position "before"
                        :icon (ic/av-movie)
                        :primary true
                        :on-click #(re-frame/dispatch [::events/search-movie @movie-name])}]]))

(defn add-movie-button []
  [ui/floating-action-button {:on-click #(re-frame/dispatch [::events/set-add-movie-modal-open true])}
   (ic/content-add {:color "white"})])

(defn remove-movie-button []
  [ui/floating-action-button {:background-color "red"
                              :on-drag-over #(.preventDefault %)
                              :on-drop      #(re-frame/dispatch [::events/delete-dragged-movie])}
   (ic/action-delete-forever)])

(defn main-panel []
  (let [list (re-frame/subscribe [::subs/list])
        list-name (re-frame/subscribe [::subs/list-name])
        open-dialog? (re-frame/subscribe [::subs/dialog-open?])
        dragged-item (re-frame/subscribe [::subs/dragged-item])]
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
       (if @dragged-item
         [remove-movie-button]
         [add-movie-button])]]]))
