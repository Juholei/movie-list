(ns movie-list.views
  (:require [re-frame.core :as re-frame]
            [movie-list.subs :as subs]
            [movie-list.events :as events]
            [movie-list.router :as router]
            [movie-list.components.movie-card :refer [movie-card]]
            [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]))

(defn share-dialog [open? link]
  [ui/dialog {:title "Share your list"
              :modal false
              :open open?
              :content-style {:width "max-content"}
              :on-request-close #(re-frame/dispatch [::events/set-share-list-dialog-open false])}
   [ui/text-field {:name     "share-link"
                   :value    link
                   :on-click #(do (.select (.-target %))
                                  (.execCommand js/document "copy")
                                  (re-frame/dispatch [::events/set-alert-message "Link copied to clipboard!"]))}]])

(defn add-movie-dialog [open?]
  (let [movie-name (re-frame/subscribe [::subs/movie-name])
        error (re-frame/subscribe [::subs/error])
        in-progress? (re-frame/subscribe [::subs/in-progress?])
        search-results (re-frame/subscribe [::subs/search-results])]
    [ui/dialog {:title "Add a movie"
                :modal false
                :open  open?
                :content-style {:width "max-content"}
                :on-request-close #(re-frame/dispatch [::events/show-add-movie-modal false])}
     [:h1 @error]
     [ui/auto-complete {:hint-text       "Type name of the movie"
                        :id              "movie-name-input"
                        :search-text     @movie-name
                        :dataSource      (if (not (or (nil? @movie-name) (zero? (count @movie-name))))
                                           (map :title @search-results)
                                           [])
                        :open            (boolean @search-results)
                        :filter          (-> js/MaterialUI .-AutoComplete .-fuzzyFilter)
                        :open-on-focus   true
                        :on-update-input #(do (re-frame/dispatch [::events/set-movie-name %])
                                              (re-frame/dispatch [::events/search-movie %]))}]
     [ui/raised-button {:label          "Add movie"
                        :id "add-movie-btn"
                        :label-position "before"
                        :icon           (if @in-progress?
                                          (ic/action-autorenew)
                                          (ic/av-movie))
                        :disabled       (or @in-progress? (= @movie-name ""))
                        :primary        true
                        :on-click       #(re-frame/dispatch [::events/retrieve-movie-by-name @movie-name])}]]))

(defn add-movie-button []
  [ui/floating-action-button {:id "open-add-movie-dialog-btn"
                              :style {:margin-left 20
                                      :margin-right 20
                                      :margin-bottom 10}
                              :on-click #(do (re-frame/dispatch [::events/set-order-list nil])
                                             (re-frame/dispatch [::events/show-add-movie-modal true])
                                             (re-frame/dispatch [::events/clear-dragged-item]))}
   (ic/content-add {:color "white"})])

(defn remove-movie-button []
  [ui/floating-action-button {:style {:margin-left 20
                                      :margin-right 20
                                      :margin-bottom 10}
                              :background-color "red"
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
        dragged-item (re-frame/subscribe [::subs/dragged-item])
        alert-message (re-frame/subscribe [::subs/alert-message])
        all-loaded? (re-frame/subscribe [::subs/all-loaded?])]
    [ui/mui-theme-provider
     {:mui-theme (get-mui-theme
                   {:palette {:text-color (color :green600)}})}
     [:div
      [ui/app-bar {:title "Movie List Maker Pro"
                   :icon-style-left {:display "none"}}]
      [add-movie-dialog @open-add-movie-dialog?]
      [share-dialog @open-share-dialog? (router/list-url @list-name (mapv :imdb-id @list))]
       [:div.container
        [ui/paper
         (if @all-loaded?
           [ui/list
            [ui/text-field {:full-width  true
                            :hint-text   "Name of the list"
                            :default-value       (or @list-name "")
                            :input-style {:text-align "center"}
                            :hint-style  {:text-align "center"
                                          :width      "100%"}
                            :on-change   #(re-frame/dispatch [::events/change-list-name (-> %
                                                                                            .-target
                                                                                            .-value)])}]
            (for [movie-data @list]
              ^{:key (:imdb-id movie-data)}
              [movie-card movie-data])]
           [ui/circular-progress {:style {:margin "auto"
                                          :padding"50px"
                                          :width "50%"
                                          :height "50%"
                                          :display "flex"
                                          :justify-content "center"
                                          :align-items "center"}}])
         [:div.align-right
          (when (and (not-empty @list) (not= @list-name ""))
            [share-list-button])
          (if @dragged-item
            [remove-movie-button]
            [add-movie-button])]]]
      [ui/snackbar {:message          (or @alert-message "")
                    :open             (boolean @alert-message)
                    :auto-hide-duration 5000
                    :on-request-close #(re-frame/dispatch [::events/set-alert-message nil])}]]]))
