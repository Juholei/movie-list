(ns movie-list.components.material-wrapper
  (:require [cljs-react-material-ui.reagent :as mui]))

(defn dialog [opts child]
  [mui/dialog opts child])

(defn text-field [opts]
  [mui/text-field opts])

(defn auto-complete [opts]
  [mui/auto-complete opts])

(def auto-complete-fuzzy-filter (-> js/MaterialUI .-AutoComplete .-fuzzyFilter))

(defn raised-button [opts]
  [mui/raised-button opts])

(defn floating-action-button [opts child]
  [mui/floating-action-button opts child])

(defn mui-theme-provider [theme child]
  [mui/mui-theme-provider theme child])

(defn app-bar [opts]
  [mui/app-bar opts])

(defn paper [child]
  [mui/paper child])

(defn mlist [xs]
  [mui/list xs])

(defn circular-progress [opts]
  [mui/circular-progress opts])

(defn snackbar [opts]
  [mui/snackbar opts])