(ns fhir-diff.core
  (:require [compojure.core :refer [wrap-routes defroutes GET POST routes context]]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [ring.adapter.jetty :as jetty]
            [ring.logger :refer [wrap-with-logger]]
            [fhir-diff.controllers.api.diff :refer [diff-handler]]
            [fhir-diff.middleware.wrap-errors :refer [wrap-api-error-handling]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.json :refer [wrap-json-response]])
  (:gen-class))


(defn api-routes []
  (routes
   (POST "/diff" {:keys [params]} (diff-handler params))))

(defroutes app-routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (wrap-routes
   (context "/api" [] (api-routes))
   wrap-api-error-handling)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-with-logger
      wrap-multipart-params
      wrap-json-response))

(defn -prod-main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -main []
  (jetty/run-jetty (wrap-reload #'app) {:port 3000}))
