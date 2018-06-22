(ns movie-list.views
  (:require [re-frame.core :as re-frame]
            [movie-list.subs :as subs]
            [movie-list.events :as events]
            [movie-list.router :as router]
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
            :on-drop       #(do (re-frame/dispatch [::events/set-order-list nil])
                                (re-frame/dispatch [::events/drag-finished movie-data]))}
   [ui/card-header {:title                  (:title movie-data)
                    :subtitle               (:year movie-data)
                    :avatar                 (:poster movie-data)
                    :act-as-expander        true
                    :show-expandable-button true}]
   [ui/card-text {:expandable true} (:plot movie-data)]])

(defn share-dialog [open? link]
  [ui/dialog {:title "Share your list"
              :modal false
              :open open?
              :on-request-close #(re-frame/dispatch [::events/set-share-list-dialog-open false])}
   [ui/text-field {:name "share-link"
                   :value link}]])

(defn add-movie-dialog [open?]
  (let [movie-name (re-frame/subscribe [::subs/movie-name])
        error (re-frame/subscribe [::subs/error])
        in-progress? (re-frame/subscribe [::subs/in-progress?])
        search-results (re-frame/subscribe [::subs/search-results])]
    [ui/dialog {:title "Add a movie"
                :modal false
                :open  open?
                :on-request-close #(re-frame/dispatch [::events/set-add-movie-modal-open false])}
     [:h1 @error]
     [ui/auto-complete {:hint-text       "Type name of the movie"
                        :search-text     @movie-name
                        :dataSource      @search-results
                        :open            (boolean @search-results)
                        :open-on-focus   true
                        :on-update-input #(do (re-frame/dispatch [::events/set-movie-name %])
                                              (re-frame/dispatch [::events/search-movie %]))}]
     [ui/raised-button {:label          "Add movie"
                        :label-position "before"
                        :icon           (if @in-progress?
                                          (ic/action-autorenew)
                                          (ic/av-movie))
                        :disabled       @in-progress?
                        :primary        true
                        :on-click       #(re-frame/dispatch [::events/retrieve-movie-by-name @movie-name])}]]))

(defn add-movie-button []
  [ui/floating-action-button {:on-click #(do (re-frame/dispatch [::events/set-order-list nil])
                                             (re-frame/dispatch [::events/set-add-movie-modal-open true]))}
   (ic/content-add {:color "white"})])

(defn remove-movie-button []
  [ui/floating-action-button {:background-color "red"
                              :on-drag-over     #(.preventDefault %)
                              :on-drop          #(do (re-frame/dispatch [::events/set-order-list nil])
                                                     (re-frame/dispatch [::events/delete-dragged-movie]))}
   (ic/action-delete-forever)])

(defn share-list-button []
  [ui/floating-action-button {:on-click #(re-frame/dispatch [::events/set-share-list-dialog-open true])}
   (ic/social-share)])

(defn main-panel []
  (let [list (re-frame/subscribe [::subs/list])
        list-name (re-frame/subscribe [::subs/list-name])
        open-add-movie-dialog? (re-frame/subscribe [::subs/dialog-open?])
        open-share-dialog? (re-frame/subscribe [::subs/share-dialog-open?])
        dragged-item (re-frame/subscribe [::subs/dragged-item])]
    [ui/mui-theme-provider
     {:mui-theme (get-mui-theme
                   {:palette {:text-color (color :green600)}})}
     [:div
      [ui/app-bar {:title "Movie List Maker Pro"}]
      [add-movie-dialog @open-add-movie-dialog?]
      [share-dialog @open-share-dialog? (router/list-url @list-name (mapv :imdb-id @list))]
      [ui/paper
       [:div.container
        [ui/list
         [ui/text-field {:hint-text "Name of the list"
                         :value         @list-name
                         :on-change #(re-frame/dispatch [::events/change-list-name (-> %
                                                                                       .-target
                                                                                       .-value)])}]
         (for [movie-data @list]
           ^{:key (:imdb-id movie-data)}
           [movie movie-data])]]
       (if @dragged-item
         [remove-movie-button]
         [add-movie-button])
       (when (and @list @list-name)
         [share-list-button])]]]))
