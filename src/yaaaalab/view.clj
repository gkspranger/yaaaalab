(ns yaaaalab.view
  (:require [yaaaalab.config :refer [->config]]
            [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]
            [yaaaalab.event :refer [emit]]))

(def views (atom {}))

(defn ->view
  [view]
  (let [adapter-view (keyword (str (symbol view) "/" (symbol (:yaaaalab.adapter.id (->config)))))
        default-view (keyword (str (symbol view) "/default"))]
    (cond
      (adapter-view @views) (adapter-view @views)
      (default-view @views) (default-view @views))))

(defn ->view-namespaces
  [namespaces]
  (filter-namespaces #".+\.views\..+" namespaces))

(defn view?
  [mapping]
  (if (:view? (meta mapping))
    true
    false))

(defn load-view
  [view]
  (let [view-meta (meta view)
        view-key (keyword (str (symbol (:id view-meta))
                               "/"
                               (symbol (:adapter view-meta))))]
    (swap! views assoc view-key view)))

(def ->namespace-view-mappings (partial filter-namespace-mappings
                                        view?))

(defn load-views
  []
  (reset! views {})
  (let [view-namespaces (->view-namespaces (all-namespaces))
        loaded-view-namespaces (load-namespaces view-namespaces)
        views (flatten (map ->namespace-view-mappings
                            loaded-view-namespaces))]
    (last (map load-view views))))

(defn apply-view
  [data apply-view-function]
  (try
    (emit :yaaaalab.event.known-view data)
    (apply-view-function data)
    (catch Exception exception
      (emit :yaaaalab.event.view-exception {:data data
                                            :exception exception}))))

(defn render
  [id data]
  (if-let [view (->view id)]
    (apply-view data view)
    (emit :yaaaalab.event.unknown-view data)))

(comment

  (load-views))
