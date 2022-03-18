(ns yaaaalab.views.help
  (:require [selmer.parser :as parser]))

(defn help
  {:view? true
   :id :help
   :adapter :default}
  [data]
  (parser/render
   (str "Displaying all commands by command group." \newline
        "{% for group, commands in items %}"
        "{{ group }}:" \newline
        "{% for command in commands %}"
        "  {{ command }}" \newline
        "{% endfor %}"
        "{% endfor %}")
   data))
