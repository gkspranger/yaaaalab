(ns yaaaalab.events.view
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-view
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.known-view}
  [data]
  (debug (str "known view (" (:yaaaalab.view.id (meta data)) ") invoked: " data)))

(defn on-unknown-view
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.unknown-view}
  [data]
  (debug (str "unknown view (" (:yaaaalab.view.id (meta data)) ") invoked: " data)))

(defn on-view-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.view-exception}
  [{:keys [data exception] :as _view-exception}]
  (error (str "view (" (:yaaaalab.view.id (meta data)) ") exception thrown: " data))
  (error exception))
