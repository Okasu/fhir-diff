(ns fhir-diff.events.routes
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 ::initialize-history
 (fn [db [_ history]]
   (assoc db :history history)))

(rf/reg-fx
 :change-history
 (fn [[path history]]
   (.setToken history path)))

(rf/reg-event-db
 ::set-current-view
 (fn [db [_ view]]
   (assoc db :current-view view)))

(rf/reg-event-fx
 ::navigate
 (fn [cofx [_ path]]
   {:dispatch [::set-current-path path]
    :change-history [path (-> cofx :db :history)]}))

(rf/reg-event-db
 ::set-current-path
 (fn [db [_ path]]
   (assoc db :current-path path)))
