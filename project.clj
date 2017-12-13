(defproject fhir-diff "0.1.0-SNAPSHOT"
  :description "FHIR Diff web application"
  :min-lein-version "2.0.0"
  :main fhir-diff.core
  :aot [fhir-diff.core]
  :uberjar-name "fhir-diff.jar"
  :source-paths ["src/clj"]
  :clean-targets ^{:protect false} ["resources/public/js/" "target"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [compojure "1.6.0"]
                 [ring/ring-json "0.4.0"]
                 [ring-logger "0.7.7"]
                 [re-frame "0.10.2"]
                 [day8.re-frame/http-fx "0.1.4"]
                 [reagent "0.7.0"]
                 [cljs-ajax "0.7.3"]
                 [ring "1.6.3"]
                 [cheshire "5.8.0"]
                 [secretary "1.2.3"]]

  :plugins [[lein-ring "0.12.1"]
            [lein-figwheel "0.5.14"]
            [lein-less "1.7.5"]
            [lein-pdo "0.1.1"]
            [lein-npm "0.6.2"]
            [lein-cljsbuild "1.1.7"]]

  :npm
  {:dependencies [bootstrap "4.0.0-beta.2"]}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}

  :aliases {"dev" ["do" "clean"
                   ["pdo"
                    ["run"]
                    ["less" "auto"]]]}

  :cljsbuild
  {:builds
   [{:id "dev"
     :source-paths ["src/cljs"]
     :figwheel {:on-jsload "fhir-diff.core/mount-root"}
     :compiler
     {:output-to "resources/public/js/core.js"
      :output-dir "resources/public/js/out"
      :source-map-timestamp true
      :preloads [devtools.preload re-frisk.preload]
      :external-config {:devtools/config {:features-to-install :all}}
      :asset-path "/js/out"
      :main fhir-diff.core}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler     {:main fhir-diff.core
                    :output-to "resources/public/js/core.js"
                    :optimizations :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print false}}]}

  :profiles
  {:uberjar
   {:main fhir-diff.core/-prod-main
    :prep-tasks [["cljsbuild" "once" "min"] ["less" "once"] "compile"]}

   :dev
   {:prep-tasks [["cljsbuild" "once" "dev"] ["less" "once"] "compile"]
    :dependencies [[com.cemerick/piggieback "0.2.1"]
                        [figwheel-sidecar "0.5.14"]
                        [binaryage/devtools "0.9.8"]
                        [re-frisk "0.5.3"]
                        [peridot "0.5.0"]]}})
