(ns yaaaalab.view
  (:require [selmer.parser :as parser]))

(def views (atom {:help (fn
                          [data]
                          (parser/render
                           (str "{% for group,commands in items %}"
                                "{{group}}" \newline
                                "{% for command in commands %}"
                                "  {{command}}" \newline
                                "{% endfor %}"
                                "{% endfor %}")
                           data))}))

(defn render
  [id data]
  ((id @views) data))

(comment

  (render :help {:items {:help '(1 2)}}))
