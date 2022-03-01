(ns yaaaalab.view
  (:require [yaaaalab.config :refer [->config]]
            [selmer.parser :as parser]))

(def views (atom {:help/default (fn
                                  [data]
                                  (parser/render
                                   (str "{% for group,commands in items %}"
                                        "{{group}}" \newline
                                        "{% for command in commands %}"
                                        "  {{command}}" \newline
                                        "{% endfor %}"
                                        "{% endfor %}")
                                   data))
                  ;; :help/shell (fn
                  ;;               [_data]
                  ;;               "i am the shell view")
                  }))

(defn render
  [id data]
  (let [adapter-view (keyword (str (symbol id) "/" (symbol (:adapter (->config)))))
        default-view (keyword (str (symbol id) "/default"))
        actual-view (if (adapter-view @views)
                      (adapter-view @views)
                      (default-view @views))]
    (actual-view data)))

(comment

  (render :help {:items {:help '(1 2)}}))
