(ns fhir-diff.views.attributes
  (:require [re-frame.core :as rf]
            [clojure.string :refer [capitalize join]]
            [fhir-diff.views.helpers :refer [column-list-items column-list]]
            [fhir-diff.subs.diff :as diff-subs]))

(defn map-difference [m1 m2]
  (apply dissoc m1 (keys m2)))

(defn map-intersection [m1 m2]
  (select-keys m1 (keys m2)))

(defn present-changes [resource]
  (when-let [{:keys [added removed]} @(rf/subscribe [::diff-subs/attributes-diff resource])]
    {:added (map-difference added removed)
     :removed (map-difference removed added)
     :changed-from (map-intersection removed added)
     :changed-to (map-intersection added removed)}))

(defn type-attribute [value]
  (let [result (->> value (map :code) (remove nil?) (join " | "))]
    (when (not (empty? result)) result)))

(defn attribute-value [attribute value]
  (case attribute
    :binding (:strength value)
    :type (type-attribute value)
    value))

(defn change-description [key from to]
  (case key
    :binding (str "Binding strength changed from " from " to " to)
    :short (str "Short description changed from \"" from "\" to \"" to "\"")
    (str (capitalize (name key)) " changed from " from " to " to)))

(defn filter-descriptions [attribute changes to]
  (for [[key value] changes
        :let [from-value (attribute-value key value)
              to-value (attribute-value key (-> to attribute key))]
        :when (and from-value to-value)]
    (change-description key from-value to-value)))

(defn change-descriptions [attribute changes to]
  (let [descriptions (filter-descriptions attribute changes to)]
    (when (not (empty? descriptions))
      [:li {:class "list-group-item list-group-item-primary"}
       [:h5 attribute]
       (for [description descriptions] ^{:key description}
         [:p description])])))

(defn change-list [from to]
  [column-list :ul "Changed"
   (for [[attribute changes] from] ^{:key attribute}
     [change-descriptions attribute changes to])])

(defn diff [resource]
  (when-let [{:keys [added removed changed-from changed-to]} (present-changes resource)]
    [:div {:class "row"}
     [column-list-items "Added" (keys added) "list-group-item-success"]
     [column-list-items "Removed" (keys removed) "list-group-item-danger"]
     [change-list changed-from changed-to]]))
