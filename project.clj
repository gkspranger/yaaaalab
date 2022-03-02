(defproject yaaaalab "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/core.async "1.5.648"]
                 [com.taoensso/timbre "5.1.2"]
                 [selmer "1.12.50"]]
  :main ^:skip-aot yaaaalab.core
  :profiles {:dev {:dependencies [[midje "1.10.5"]]
                   :plugins [[lein-midje "3.2.2"]]}})
