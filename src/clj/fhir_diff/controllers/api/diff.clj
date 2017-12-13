(ns fhir-diff.controllers.api.diff
  (:require [cheshire.core :refer [parse-string]]
            [fhir-diff.operations.diff :refer [diff]]))

(defn slurp-tempfile [name params]
  (slurp (get-in params [name :tempfile])))

(defn diff-handler [params]
  (let [data-a (parse-string (slurp-tempfile "file-a" params))
        data-b (parse-string (slurp-tempfile "file-b" params))]
    {:body (diff data-a data-b)}))
