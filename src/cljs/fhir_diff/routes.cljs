(ns fhir-diff.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [goog.events :as gevents]
            [goog.history.EventType :as EventType]
            [fhir-diff.events.routes :as events]
            [fhir-diff.views.diff :refer [diff]]
            [fhir-diff.views.uploader :refer [uploader]]
            [re-frame.core :as rf]))

(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (rf/dispatch [::events/set-current-view [uploader]])
    (rf/dispatch [::events/set-current-path "/"]))

  (defroute "/diff" []
    (rf/dispatch [::events/set-current-view [diff]])
    (rf/dispatch [::events/set-current-path "/diff"]))

  (rf/dispatch-sync [::events/initialize-history (hook-browser-navigation!)]))
