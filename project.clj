(defproject yaaaalab "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :main ^:skip-aot yaaaalab.core
  :profiles {:dev {:dependencies [[midje "1.10.5"]]
                   :plugins [[lein-midje "3.2.2"]]}})
