(ns fhir-diff.views.app
  (:require [re-frame.core :as rf]
            [fhir-diff.subs.routes :as routes]
            [fhir-diff.subs.diff :as diff-subs]
            [fhir-diff.views.diff :refer [diff]]
            [fhir-diff.views.uploader :refer [uploader]]))

(defn tab-attributes [path]
  (let [active (or (and (= @(rf/subscribe [::routes/current-path]) path) "active")  "")]
    {:class (str "nav-link " active) :href (str "#" path)}))

(defn app []
  (let [current-view (rf/subscribe [::routes/current-view])]
    [:div
     [:nav {:class "navbar navbar-dark bg-dark"}
      [:a {:class "navbar-brand" :href "#/"} "FHIR Diff"]]
     [:ul {:class "nav nav-tabs"}
      [:li {:class "nav-item"}
       [:a (tab-attributes "/") "Upload"]]
      (when @(rf/subscribe [::diff-subs/diff])
        [:li {:class "nav-item"}
         [:a (tab-attributes "/diff") "Diff"]])]
     @current-view]))
