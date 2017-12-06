(ns fhir-diff.core
  (:require [reagent.core :as r]
            [fhir-diff.events.db :as db-events]
            [fhir-diff.views.app :refer [app]]
            [fhir-diff.routes :refer [routes]]
            [re-frame.core :as rf]))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (r/render [app] (.getElementById js/document "app")))

(defn ^:export init []
  (rf/dispatch-sync [::db-events/initialize])
  (routes)
  (mount-root))
