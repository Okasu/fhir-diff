(ns fhir-diff.operations.diff)

(def definitions
  (comp
   (map #(get % "resource"))
   (filter #(= (get % "resourceType") "StructureDefinition"))))

(defn definition-names [data]
  (into (sorted-set)
        (comp definitions (map #(get % "name")))
        (get data "entry")))
