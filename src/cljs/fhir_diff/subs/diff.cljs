(ns fhir-diff.subs.diff
  (:require [re-frame.core :as rf]))

(rf/reg-sub ::diff (fn [db _] (:diff db)))

(rf/reg-sub ::uploading? (fn [db] (:fhir-files-uploading? db)))

(rf/reg-sub ::added-definitions (fn [db _] (-> db :diff :added-definitions)))

(rf/reg-sub ::removed-definitions (fn [db _] (-> db :diff :removed-definitions)))
