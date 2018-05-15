(ns movie-list.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  ::list
  (fn [db]
    (:list db)))

(re-frame/reg-sub
  ::list-name
  (fn [db]
    (:list-name db)))

(re-frame/reg-sub
  ::dialog-open?
  (fn [db]
    (:dialog-open? db)))
