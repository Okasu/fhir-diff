(ns fhir-diff.middleware.wrap-errors
  (:import com.fasterxml.jackson.core.JsonParseException))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body data})

(defn wrap-api-error-handling [handler]
  (fn [req]
    (try
      (or (handler req) (json-response {"error" "Not Found"} 404))
      (catch JsonParseException e
        (json-response {"error" "Invalid JSON"} 400)))))
