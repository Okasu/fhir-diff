(ns fhir-diff.views.diff
  (:require [re-frame.core :as rf]
            [fhir-diff.subs.diff :as diff-subs]))

(defn definitions [sub class]
  (for [definition @(rf/subscribe [sub])] ^{:key definition}
    [:li {:class (str "list-group-item " class)}
     definition]))

(defn diff []
  [:div {:class "row"}
   [:div {:class "col-sm"}
    [:h3 "Added"]
    [:ul {:class "list-group"}
     (definitions ::diff-subs/added-definitions "list-group-item-success")]]

   [:div {:class "col-sm"}
    [:h3 "Removed"]
    [:ul {:class "list-group"}
     (definitions ::diff-subs/removed-definitions "list-group-item-danger")]]])
