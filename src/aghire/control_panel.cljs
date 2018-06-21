(ns aghire.control-panel
  (:require
    [reagent.core :as rfr]
    [aghire.utility :refer [target-val] :as utl]
    [aghire.db :as db]
    [aghire.job-listing-control-bar :as jlcb]
    [aghire.filtering :as flt]
    [aghire.regex-ui :as rgxui]
    [aghire.sorting :as sort]))


;;; --- the beef -----------------------------------------------------

(defn control-panel []
  (fn []
    [:div {:style {:background "#ffb57d"}}

     [utl/open-shut-case :show-filters "Filters"
      flt/mk-title-selects
      flt/mk-user-selects]

     [rgxui/mk-regex-search]

     [sort/sort-bar]

     [jlcb/job-listing-control-bar]]))

