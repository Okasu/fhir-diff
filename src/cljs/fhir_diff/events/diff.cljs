(ns fhir-diff.events.diff
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [fhir-diff.events.routes :as routes]
            [ajax.core :refer [json-response-format]]))

(rf/reg-event-fx
 ::send-files-success
 (fn [cofx [_ data]]
   {:db (assoc (:db cofx) :diff data :fhir-files-uploading? false)
    :dispatch [::routes/navigate "/diff"]}))

(rf/reg-event-fx
 ::send-files
 (fn [cofx [_ files]]
   {:db (assoc (:db cofx) :fhir-files-uploading? true)
    :http-xhrio {:method :post
                 :uri "/api/diff"
                 :body files
                 :response-format (json-response-format {:keywords? true})
                 :on-success [::send-files-success]}}))
