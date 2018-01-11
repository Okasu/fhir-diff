(ns fhir-diff.controllers.api.diff
  (:import com.fasterxml.jackson.core.JsonParseException)
  (:require [cheshire.core :refer [parse-string]]
            [fhir-diff.operations.diff :refer [diff]]))

(defn slurp-tempfile [name params]
  (slurp (get-in params [name :tempfile])))

(defn diff-handler [params]
  (try
    (let [data-a (parse-string (slurp-tempfile "file-a" params))
          data-b (parse-string (slurp-tempfile "file-b" params))]
      {:body (diff data-a data-b)})
    (catch JsonParseException _
      {:status 400
       :body {:error "Invalid JSON"}})))
