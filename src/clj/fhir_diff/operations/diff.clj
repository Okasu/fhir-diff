(ns fhir-diff.operations.diff
  (:require [cheshire.core :refer [parse-string]]
            [clojure.data :as d]))

(def attribute-whitelist
  ["min" "max" "type" "short" "binding"])

(defn definition-elements [definition]
  (into {} (map #(vector (get % "path") (select-keys % attribute-whitelist))
                (get-in definition ["snapshot" "element"]))))

(defn definition-attributes [definition]
  [(get definition "name")
   (definition-elements definition)])

(def extract-definitions
  (comp
   (map #(get % "resource"))
   (filter #(= (get % "resourceType") "StructureDefinition"))
   (map definition-attributes)))

(defn definition-names [definitions]
  (into (sorted-set) (keys definitions)))

(defn definitions [data]
  (into {} extract-definitions (get data "entry")))

(defn resource-diff [definitions-a definitions-b]
  (d/diff (definition-names definitions-a) (definition-names definitions-b)))

(defn attributes-diff [definitions-a definitions-b changed-resources]
  (reduce (fn [diff resource]
            (let [[removed added] (d/diff (get definitions-a resource)
                                          (get definitions-b resource))]
              (if (and removed added)
                (assoc diff resource {:added added :removed removed})
                diff)))
          (sorted-map) changed-resources))

(defn diff [data-a data-b]
  (let [definitions-a (definitions data-a)
        definitions-b (definitions data-b)
        [removed-resources added-resources changed-resources]
          (resource-diff definitions-a definitions-b)
        changes (attributes-diff definitions-a definitions-b changed-resources)]
    {:added-resources added-resources
     :removed-resources removed-resources
     :changed-resources changes}))
