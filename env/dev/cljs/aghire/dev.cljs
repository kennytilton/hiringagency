(ns ^:figwheel-no-load aghire.dev
  (:require
    [aghire.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
