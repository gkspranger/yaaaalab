(ns yaaaalab.views.help
  (:require [selmer.parser :as parser]))

(defn help
  {:view? true
   :id :help
   :adapter :default}
  [data]
  (parser/render
   (str "some long blurb about how this is help this bot will help them somehow" \newline
        "{% for group, commands in items %}"
        "{{group}}" \newline
        "{% for command in commands %}"
        "  {{command}}" \newline
        "{% endfor %}"
        "{% endfor %}")
   data))
