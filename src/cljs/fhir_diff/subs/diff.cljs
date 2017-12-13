(ns fhir-diff.subs.diff
  (:require [re-frame.core :as rf]))

(rf/reg-sub ::diff (fn [db _] (:diff db)))

(rf/reg-sub ::selected-resource (fn [db _] (:selected-resource db)))

(rf/reg-sub
 ::attributes-diff
 (fn [db [_ resource]]
   (-> db :diff :changed-resources (get (keyword resource)))))

(rf/reg-sub ::uploading? (fn [db] (:fhir-files-uploading? db)))

(rf/reg-sub ::added-resources (fn [db _] (-> db :diff :added-resources)))

(rf/reg-sub ::removed-resources (fn [db _] (-> db :diff :removed-resources)))

(rf/reg-sub
 ::changed-resources
 (fn [db _]
   (->> db :diff :changed-resources (into (sorted-map)))))
