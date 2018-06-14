(ns movie-list.subs
  (:require [re-frame.core :as re-frame]))

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

(re-frame/reg-sub
  ::movie-name
  (fn [db]
    (:movie-name db)))

(re-frame/reg-sub
  ::dragged-item
  (fn [{dragged-item :dragged-item}]
    dragged-item))

(re-frame/reg-sub
  ::error
  (fn [{error :error}]
    error))
