(ns yaaaalab.config)

(def config {:adapter :shell
             :prefix "!"
             :log-level :info})

(defn ->config
  []
  config)
