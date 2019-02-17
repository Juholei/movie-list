(ns movie-list.components.movie-card
  (:require ["@material-ui/core" :as ui]
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
    [:> ui/Card {:style {:background "transparent"}}
     [:> ui/CardHeader {:title                  (:title movie-data)
                        :subheader               (:year movie-data)}]
     [:> ui/CardContent #_{:expandable true} (:plot movie-data)]]]])
