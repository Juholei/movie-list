(ns movie-list.components.movie-card
  (:require [cljs-react-material-ui.reagent :as ui]
            [re-frame.core :as re-frame]
            [movie-list.events :as events]))

(defn movie-card [movie-data]
  [:div.wrapper {:draggable     true
                 :on-drag-over  #(.preventDefault %)
                 :on-drag-start #(re-frame/dispatch [::events/set-dragged-item movie-data])
                 :on-drag-end   #(re-frame/dispatch [::events/clear-dragged-item])
                 :on-drop       #(do (re-frame/dispatch [::events/set-order-list nil])
                                     (re-frame/dispatch [::events/drag-finished movie-data]))}
   [:div.blur {:style {:background-image (str "url(" (:poster movie-data) ")")}}]
   [:div.content
    [ui/card {:style {:background "transparent"}}
     [ui/card-header {:title                  (:title movie-data)
                      :subtitle               (:year movie-data)
                      :avatar                 (:poster movie-data)
                      :act-as-expander        true
                      :show-expandable-button true}]
     [ui/card-text {:expandable true} (:plot movie-data)]]]])
