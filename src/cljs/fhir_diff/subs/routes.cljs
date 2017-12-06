(ns fhir-diff.subs.routes
  (:require [re-frame.core :as rf]))

(rf/reg-sub ::current-path (fn [db _] (:current-path db)))

(rf/reg-sub ::current-view (fn [db _] (:current-view db)))
