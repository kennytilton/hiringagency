(ns aghire.control-panel
  (:require [reagent.core :as rfr]
            [aghire.filtering :as flt]
            [aghire.utility :refer [target-val] :as utl]
    [aghire.job-listing-control-bar :as jlcb]
    #_[aghire.regex-search :as rgx]
            [aghire.db :as db]))

;;; --- job sort bar -----------------------------------------------------------

(defn sort-bar-option []
  (fn [{:keys [title] :as jsort}]
    (let [curr-sort @db/job-sort]
      [:button.sortOption
       {:style    {:color (if (= title (:title curr-sort))
                            "blue" "#222")}
        :selected (= jsort curr-sort)
        :on-click (fn []
                    (if (= title (:title curr-sort))
                      (reset! db/job-sort (update curr-sort :order #(* -1 %)))
                      (reset! db/job-sort jsort)))}
       (str (:title jsort) (if (= title (:title curr-sort))
                             (if (= (:order curr-sort) -1)
                               (utl/unesc "&#x2798") (utl/unesc "&#x279a"))))])))

(defn sort-bar []
  (fn []
    [:div {:style {:padding 0
                   :margin  "15px 0 0 24px"
                   :display "flex"}}
     [:span {:style {:margin-right "6px"}} "Sort by:"]
     (into [:ul {:style (merge utl/hz-flex-wrap
                    {:list-style "none"
                     :padding    0
                     :margin     0})}]
      (map (fn [jsort]
             [:li [sort-bar-option jsort]])
        utl/job-sorts))]))

;;; --- the beef -----------------------------------------------------

(defn control-panel []
  (fn []
    [:div {:style {:background "#ffb57d"}}

     [utl/open-shut-case :show-filters "Filters"
      flt/mk-title-selects
      flt/mk-user-selects]

     #_ [rgx/mk-regex-search]

     [sort-bar]

     [jlcb/job-listing-control-bar]]))

