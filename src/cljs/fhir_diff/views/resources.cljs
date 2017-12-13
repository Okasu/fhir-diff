(ns fhir-diff.views.resources
  (:require [re-frame.core :as rf]
            [fhir-diff.views.helpers :refer [column-list column-list-items]]
            [fhir-diff.subs.diff :as diff-subs]))

(defn resource-changes [name sub class]
  [column-list-items name @(rf/subscribe [sub]) class])

(defn diff []
  [:div {:class "row"}
   [resource-changes "Added" ::diff-subs/added-resources "list-group-item-success"]
   [resource-changes "Removed" ::diff-subs/removed-resources "list-group-item-danger"]
   [column-list :div "Changed"
    (for [[resource] @(rf/subscribe [::diff-subs/changed-resources])] ^{:key resource}
      [:a {:href (str "#/diff/" (name resource))
           :class "list-group-item list-group-item-action list-group-item-primary"}
       resource])]])
