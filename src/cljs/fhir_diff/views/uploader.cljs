(ns fhir-diff.views.uploader
  (:require [re-frame.core :as rf]
            [fhir-diff.subs.diff :as diff-subs]
            [fhir-diff.events.diff :as diff-events]))

(defn on-uploader-submit [e]
  (.preventDefault e)
  (rf/dispatch [::diff-events/send-files (js/FormData. (.-target e))]))

(defn uploader []
  (let [uploading? @(rf/subscribe [::diff-subs/uploading?])]
    [:div {:class "row"}
     [:div {:class "col-md"}
      [:h2 "Usage"]
      [:p {:class "text-justify"}
       "To compare two FHIR standards you will need to download FHIR definitions in
      JSON format(latest one can be found "
       [:a {:href "https://www.hl7.org/fhir/definitions.json.zip"} "here"]
       ") unpack it and upload two "
       [:kbd "profiles-resources.json"]
       " files - one for old standard
      and another one for new standard to application via form below."]
      [:p {:class "text-justify"}
       "Also you can download unpacked JSON files with FHIR definitions for versions
      1.0.2 and 1.8.0 from "
       [:a {:href "https://github.com/Okasu/fhir-diff/tree/master/test-resources"} "here"]
       "."]
      [:hr]
      [:form {:on-submit on-uploader-submit}
       [:div {:class "form-group"}
        [:label "Structure Difinition A"]
        [:input {:class "form-control-file" :type "file" :name "file-a" :required true}]]
       [:div {:class "form-group"}
        [:label "Structure Difinition B"]
        [:input {:class "form-control-file" :type "file" :name "file-b" :required true}]]
       [:button {:class "btn btn-primary" :type "submit" :disabled uploading?}
        (if uploading? "Uploading..." "Submit")]]]]))
