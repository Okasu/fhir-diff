(ns fhir-diff.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [goog.events :as gevents]
            [goog.history.EventType :as EventType]
            [fhir-diff.events.routes :as route-events]
            [fhir-diff.events.diff :as diff-events]
            [fhir-diff.views.resources :as resources]
            [fhir-diff.views.attributes :as attributes]
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
    (rf/dispatch [::route-events/set-current-view [uploader]])
    (rf/dispatch [::route-events/set-current-path "/"]))

  (defroute "/diff" []
    (rf/dispatch [::route-events/set-current-view [resources/diff]])
    (rf/dispatch [::route-events/set-current-path "/diff"]))

  (defroute "/diff/:resource" [resource]
    (rf/dispatch [::diff-events/select-resource resource])
    (rf/dispatch [::route-events/set-current-view [attributes/diff resource]])
    (rf/dispatch [::route-events/set-current-path (str "/diff/" resource)]))

  (rf/dispatch-sync [::route-events/initialize-history (hook-browser-navigation!)]))
