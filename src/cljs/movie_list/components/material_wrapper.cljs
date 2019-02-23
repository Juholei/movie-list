(ns movie-list.components.material-wrapper
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/core/styles" :refer [createMuiTheme withStyles]]
            ["@material-ui/core/colors" :as mui-colors]
            [reagent.core :as r]
            [goog.object :as gobj]
            [reagent.impl.template :as rtpl]))

;; TextField cursor fix:

(def ^:private input-component
  (r/reactify-component
    (fn [props]
      [:input (-> props
                  (assoc :ref (:inputRef props))
                  (dissoc :inputRef))])))

(def ^:private textarea-component
  (r/reactify-component
    (fn [props]
      [:textarea (-> props
                     (assoc :ref (:inputRef props))
                     (dissoc :inputRef))])))

;; To fix cursor jumping when controlled input value is changed,
;; use wrapper input element created by Reagent instead of
;; letting Material-UI to create input element directly using React.
;; Create-element + convert-props-value is the same as what adapt-react-class does.
(defn text-field [props & children]
  (let [props (-> props
                  (assoc-in [:InputProps :inputComponent] (cond
                                                            (and (:multiline props) (:rows props) (not (:maxRows props)))
                                                            textarea-component

                                                            ;; FIXME: Autosize multiline field is broken.
                                                            (:multiline props)
                                                            nil

                                                            ;; Select doesn't require cursor fix so default can be used.
                                                            (:select props)
                                                            nil

                                                            :else
                                                            input-component))
                  rtpl/convert-prop-value)]
    (apply r/create-element mui/TextField props (map r/as-element children))))

(defn dialog [opts content dialog-actions]
  [:> mui/Dialog opts
   [:> mui/DialogTitle (:title opts)]
   [:> mui/DialogContent content]
   [:> mui/DialogActions dialog-actions]])
;
#_(defn text-field [opts]
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
