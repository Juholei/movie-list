(ns movie-list.views
  (:require [re-frame.core :as re-frame]
            [movie-list.subs :as subs]
            [movie-list.events :as events]
            [movie-list.router :as router]
            [movie-list.components.movie-card :refer [movie-card]]
            [movie-list.components.material-wrapper :as m]
            [movie-list.components.material-ui-icon-wrapper :as icon]))

(defn share-dialog [open? link]
  [m/dialog {:title      "Share your list"
             :open       open?
             :full-width true
             :max-width  "sm"
             :on-close   #(re-frame/dispatch [::events/set-share-list-dialog-open false])}
   [m/text-field {:name       "share-link"
                  :value      link
                  :full-width true
                  :on-click   #(do (.select (.-target %))
                                   (.execCommand js/document "copy")
                                   (re-frame/dispatch [::events/set-alert-message "Link copied to clipboard!"]))}]])

(defn add-movie-dialog [open?]
  (let [movie-name (re-frame/subscribe [::subs/movie-name])
        error (re-frame/subscribe [::subs/error])
        in-progress? (re-frame/subscribe [::subs/in-progress?])
        search-results (re-frame/subscribe [::subs/search-results])]
    [m/dialog {:title    "Add a movie"
               :open     open?
               :full-width true
               :max-width "sm"
               :on-close #(re-frame/dispatch [::events/show-add-movie-modal false])}
     [:<>
      [:h1 @error]
      [m/auto-complete {:placeholder "Type name of the movie"
                        :id          "movie-name-input"
                        :value       (or @movie-name "")
                        :full-width  true
                        :auto-focus  true
                        :on-change   #(let [value (-> % .-target .-value)]
                                        (re-frame/dispatch [::events/set-movie-name value])
                                        (re-frame/dispatch [::events/search-movie value]))}]]
     [m/button {:label    "Add movie"
                :id       "add-movie-btn"
                :disabled (or @in-progress? (= @movie-name ""))
                :color    "primary"
                :on-click #(re-frame/dispatch [::events/retrieve-movie-by-name @movie-name])}]]))

(defn add-movie-button []
  [m/floating-action-button {:color "primary"
                             :id       "open-add-movie-dialog-btn"
                             :style    {:margin-left   20
                                        :margin-right  20
                                        :margin-bottom 10}
                             :on-click #(do (re-frame/dispatch [::events/set-order-list nil])
                                            (re-frame/dispatch [::events/show-add-movie-modal true])
                                            (re-frame/dispatch [::events/clear-dragged-item]))}
   [icon/content-add {:color "primary"}]])

(defn remove-movie-button []
  [m/floating-action-button {:id               "delete-movie-btn"
                             :style            {:margin-left   20
                                                :margin-right  20
                                                :margin-bottom 10}
                             :color "secondary"
                             :on-drag-over     #(.preventDefault %)
                             :on-drop          #(do (re-frame/dispatch [::events/set-order-list nil])
                                                    (re-frame/dispatch [::events/delete-dragged-movie]))}
   [icon/action-delete-forever]])

(defn share-list-button []
  [m/floating-action-button {:on-click #(re-frame/dispatch [::events/set-share-list-dialog-open true])
                             :color    "secondary"
                             :style    {:margin-bottom 10}}
   [icon/social-share]])

(defn main-panel []
  (let [list (re-frame/subscribe [::subs/list])
        list-name (re-frame/subscribe [::subs/list-name])
        open-add-movie-dialog? (re-frame/subscribe [::subs/dialog-open?])
        open-share-dialog? (re-frame/subscribe [::subs/share-dialog-open?])
        dragged-item (re-frame/subscribe [::subs/dragged-item])
        alert-message (re-frame/subscribe [::subs/alert-message])
        all-loaded? (re-frame/subscribe [::subs/all-loaded?])]
    [:<>
     [m/app-bar {:title           "Movie List Maker Pro"
                 :icon-style-left {:display "none"}}]
     [add-movie-dialog @open-add-movie-dialog?]
     [share-dialog @open-share-dialog? (router/list-url @list-name (mapv :imdb-id @list))]
     [:div.container
      [m/paper
       [:<>
        (if @all-loaded?
          [:<>
           [m/text-field {:full-width  true
                          :placeholder "Name of the list"
                          :value       (or @list-name "")
                          :on-change   #(re-frame/dispatch [::events/change-list-name (-> %
                                                                                          .-target
                                                                                          .-value)])}]
           [m/mlist
            (for [movie-data @list]
              ^{:key (:imdb-id movie-data)}
              [movie-card movie-data])]]
          [m/circular-progress])
        [:div.align-right
         (when (and (not-empty @list) (not= @list-name ""))
           [share-list-button])
         (if @dragged-item
           [remove-movie-button]
           [add-movie-button])]]]]
     [m/snackbar {:message            (or @alert-message "")
                  :open               (boolean @alert-message)
                  :auto-hide-duration 5000
                  :on-close           #(re-frame/dispatch [::events/set-alert-message nil])}]]))
