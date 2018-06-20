(ns aghire.filtering
  (:require

    [aghire.month-loader :as loader]))

(defn jobs-filtered []
  @loader/month-jobs)