(ns fhir-diff.events.diff
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [fhir-diff.events.routes :as routes]
            [ajax.core :refer [json-response-format]]))

(rf/reg-fx
 :alert
 (fn [[text]]
   (js/alert text)))

(rf/reg-event-fx
 ::send-files-success
 (fn [cofx [_ data]]
   {:db (assoc (:db cofx) :diff data :fhir-files-uploading? false)
    :dispatch-n [[::select-resource nil]
                 [::routes/navigate "/diff"]]}))

(rf/reg-event-db
 ::select-resource
 (fn [db [_ resource]]
   (assoc db :selected-resource resource)))

(rf/reg-event-fx
 ::send-files-error
 (fn [cofx _]
   {:db (assoc (:db cofx) :fhir-files-uploading? false)
    :alert
    ["Please, upload valid resource profiles JSON file, see main page for examples!"]}))

(rf/reg-event-fx
 ::send-files
 (fn [cofx [_ files]]
   {:db (assoc (:db cofx) :fhir-files-uploading? true)
    :http-xhrio {:method :post
                 :uri "/api/diff"
                 :body files
                 :response-format (json-response-format {:keywords? true})
                 :on-failure [::send-files-error]
                 :on-success [::send-files-success]}}))
