(ns fhir-diff.events.db
  (:require [re-frame.core :as rf]))

(rf/reg-event-db ::initialize (fn [_ _] {:current-path "/"}))
