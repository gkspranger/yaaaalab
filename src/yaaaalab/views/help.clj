(ns yaaaalab.views.help
  (:require [selmer.parser :as parser]))

(defn help
  {:yaaaalab.view.view? true
   :yaaaalab.view.id :yaaaalab.view.help
   :yaaaalab.view.adapter.id :default}
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
