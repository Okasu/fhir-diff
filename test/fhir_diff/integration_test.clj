(ns fhir-diff.integration-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :refer [resource file]]
            [peridot.core :refer :all]
            [cheshire.core :refer [parse-string]]
            [fhir-diff.core :refer [app]]))

;;
;; Helpers
;;

(def correct-added-resources
  ["ActivityDefinition" "CapabilityStatement" "CareTeam" "CodeSystem"
   "CompartmentDefinition" "Consent" "DiagnosticRequest" "Endpoint"
   "ExpansionProfile" "GuidanceResponse" "ImagingManifest" "Library"
   "Linkage" "Measure" "MeasureReport" "MedicationRequest" "MessageDefinition"
   "MetadataResource" "NutritionRequest" "PlanDefinition" "PractitionerRole"
   "RequestGroup" "ResearchStudy" "ResearchSubject" "Sequence" "ServiceDefinition"
   "StructureMap" "Task" "TestReport"])

(def correct-removed-resources
  ["Conformance" "DiagnosticOrder" "ImagingObjectSelection" "MedicationOrder"
   "NutritionOrder" "Order" "OrderResponse"])

(def correct-changed-resources
  #{"Account", "AllergyIntolerance", "Appointment", "AppointmentResponse",
    "AuditEvent", "Basic", "Binary", "BodySite", "Bundle", "CarePlan", "Claim",
    "ClaimResponse", "ClinicalImpression", "Communication",
    "CommunicationRequest", "Composition", "ConceptMap", "Condition", "Contract",
    "Coverage", "DataElement", "DetectedIssue", "Device", "DeviceComponent",
    "DeviceMetric", "DeviceUseRequest", "DeviceUseStatement", "DiagnosticReport",
    "DocumentManifest", "DocumentReference", "DomainResource",
    "EligibilityRequest", "EligibilityResponse", "Encounter", "EnrollmentRequest",
    "EnrollmentResponse", "EpisodeOfCare", "ExplanationOfBenefit",
    "FamilyMemberHistory", "Flag", "Goal", "Group", "HealthcareService",
    "ImagingStudy", "Immunization", "ImmunizationRecommendation",
    "ImplementationGuide", "List", "Location", "Media", "Medication",
    "MedicationAdministration", "MedicationDispense", "MedicationStatement",
    "MessageHeader", "NamingSystem", "Observation", "OperationDefinition",
    "OperationOutcome", "Organization", "Parameters", "Patient", "PaymentNotice",
    "PaymentReconciliation", "Person", "Practitioner", "Procedure",
    "ProcedureRequest", "ProcessRequest", "ProcessResponse", "Provenance",
    "Questionnaire", "QuestionnaireResponse", "ReferralRequest", "RelatedPerson",
    "Resource", "RiskAssessment", "Schedule", "SearchParameter", "Slot",
    "Specimen", "StructureDefinition", "Subscription", "Substance",
    "SupplyDelivery", "SupplyRequest", "TestScript", "ValueSet",
    "VisionPrescription"})

(defn test-resource [path]
  (file (str "test-resources/" path)))

(defn app-request [& args]
  (:response (apply request (conj args (session app)))))

(defn diff-response [params]
  (app-request "/api/diff" :request-method :post :params params))

(defn diff-response-body [params]
  (-> (diff-response params) :body parse-string))

;;
;; Tests
;;

(deftest test-static-routes
  (testing "main route"
    (let [response (app-request "/")]
      (is (= (:status response) 200))
      (is (= (slurp (:body response)) (slurp (resource "public/index.html"))))))

  (testing "not-found route"
    (let [response (app-request "/invalid")]
      (is (= (:status response) 404)))))

(deftest test-api-diff
  (testing "integration"
    (let [file-a (test-resource "profiles-resources-1.0.2.json")
          file-b (test-resource "profiles-resources-1.8.0.json")
          diff (diff-response-body {"file-a" file-a "file-b" file-b})]
      (is (= correct-added-resources (get diff "added-resources")))
      (is (= correct-removed-resources (get diff "removed-resources")))
      (is (= correct-changed-resources
             (->> (get diff "changed-resources") keys (map name) (into (sorted-set)))))))

  (testing "invalid json"
    (let [invalid (test-resource "invalid.json")
          diff (diff-response {"file-a" invalid "file-b" invalid})]
      (is (= (:status diff) 400))))

  (testing "handle empty json"
    (let [empty (test-resource "empty.json")
          diff (diff-response {"file-a" empty "file-b" empty})]
      (is (= (parse-string (:body diff))
             {"added-resources" nil "removed-resources" nil "changed-resources" {}}))
      (is (= (:status diff) 200)))))
