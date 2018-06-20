(ns aghire.db
  (:require [clojure.string :as str]
            [reagent.core :as r]))

(def app (r/atom
           (merge
             {:month-load nil
              :job-display-max        42
              :job-sort               nil
              :filter-active {}
              :show-filters           true
              :show-filtered-excluded false
              :rgx-match-case         false
              :rgx-xlate-or-and       true
              :search-history         {}
              :show-job-details       {}})))


(def job-sort (r/cursor app [:job-sort]))
(def job-display-max (r/cursor app [:job-display-max]))
(def filter-active (r/cursor app [:filter-active]))
(def show-filters (r/cursor app [:show-filters]))
(def show-filter-excluded (r/cursor app [:show-filter-excluded]))
(def rgx-match-case (r/cursor app [:rgx-match-case]))
(def rgx-xlate-or-and (r/cursor app [:rgx-xlate-or-and]))
(def search-history (r/cursor app [:search-history]))
(def show-job-details (r/cursor app [:show-job-details]))

;(rfr/reg-event-fx ::initialize-db
;  [(rfr/inject-cofx :storage-user-notes)]
;
;  (fn [{:keys [storage-user-notes]} _]
;    (merge
;      {:db (assoc (initial-db) :user-notes storage-user-notes)}
;      (when-let [initial-month (nth (loader/gMonthlies-cljs) js/initialSearchMoIdx)]
;        {:dispatch [:month-set (:hnId initial-month)]}))))

