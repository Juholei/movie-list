(ns movie-list.events
  (:require [re-frame.core :as re-frame]
            [movie-list.db :as db]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
  ::set-dragged-item
  (fn [db [_ item]]
    (assoc db :dragged-item item)))

(re-frame/reg-event-db
  ::clear-dragged-item
  (fn [db _]
    (dissoc db :dragged-item)))

(re-frame/reg-event-db
  ::drag-finished
  (fn [{:keys [dragged-item] :as db} [_ destination]]
    (update db :list (fn [list]
                       (let [destination-index (.indexOf list destination)
                            [start end] (split-at destination-index (remove #{dragged-item} list))]
                         (concat start [dragged-item] end))))))

(re-frame/reg-event-db
  ::open-add-movie-modal
  (fn [db _]
    (assoc db :dialog-open? true)))
