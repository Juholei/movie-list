(ns movie-list.components.material-ui-icon-wrapper
  (:require ["@material-ui/icons" :as mui-icons]))

;(defn action-autorenew [& args]
;  (ic/action-autorenew args))
;
;(defn av-movie [& args]
;  (ic/av-movie args))
;
(defn content-add [& args]
  [:> mui-icons/Add args])

(defn action-delete-forever [& args]
  [:> mui-icons/DeleteForever args])

(defn social-share [& args]
  [:> mui-icons/Share args])
