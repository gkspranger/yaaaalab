(defproject yaaaalab "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [com.taoensso/timbre "5.1.2"]
                 [selmer "1.12.50"]
                 [org.clojure/alpha.spec "99456b1856a6fd934e2c30b17920bd790dd81775"]]
  :repositories [["public-github" {:url "git://github.com"}]]
  :git-down {org.clojure/alpha.spec {:coordinates clojure/spec-alpha2}}
  :middleware [lein-git-down.plugin/inject-properties]

  :main ^:skip-aot yaaaalab.core

  :profiles {:dev {:dependencies [[midje "1.10.5"]]
                   :plugins [[lein-midje "3.2.2"]
                             [lein-cloverage "1.1.2"]
                             [reifyhealth/lein-git-down "0.4.1"]]}}
  
  :aliases {"test" ["midje"]})
