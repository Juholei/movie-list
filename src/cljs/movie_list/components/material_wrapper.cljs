(ns movie-list.components.material-wrapper
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/core/styles" :refer [createMuiTheme withStyles]]
            ["@material-ui/core/colors" :as mui-colors]))

(defn dialog [opts content dialog-actions]
  [:> mui/Dialog opts
   [:> mui/DialogTitle (:title opts)]
   [:> mui/DialogContent content]
   [:> mui/DialogActions dialog-actions]])
;
(defn text-field [opts]
  [:> mui/TextField opts])
;
(defn auto-complete [opts]
  ; Not auto-completing anything at the moment, as Material UI dropped
  ; their own implementation of it.
  [text-field opts])

(defn button [opts]
  [:> mui/Button opts
   (:label opts)])
;
(defn floating-action-button [opts child]
  [:> mui/Fab opts child])

(defn app-bar [opts]
  [:> mui/AppBar {:position "static"}
   [:> mui/Toolbar
    [:> mui/Typography {:variant "h6"
                        :color   "inherit"} (:title opts)]]])
;
(defn paper [child]
  [:> mui/Paper child])
;
(defn mlist [xs]
  [:> mui/List xs])

(defn circular-progress [opts]
  [:> mui/CircularProgress opts])

(defn snackbar [opts]
  [:> mui/Snackbar opts])
