(ns fhir-diff.controllers.api.diff
  (:require [cheshire.core :refer [parse-string]]
            [fhir-diff.operations.diff :refer :all]
            [clojure.set :refer [difference]]))

(defn slurp-tempfile [name params]
  (slurp (get-in params [name :tempfile])))

(defn diff-handler [params]
  (let [data-a (parse-string (slurp-tempfile "file-a" params))
        data-b (parse-string (slurp-tempfile "file-b" params))
        definitions-a (definition-names data-a)
        definitions-b (definition-names data-b)]
    {:body
     {:added-definitions (difference definitions-b definitions-a)
      :removed-definitions (difference definitions-a definitions-b)}}))
