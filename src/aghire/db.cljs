(ns aghire.db
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [aghire.utility :as utl]
            ))

(def app (r/atom
           (merge
             {:month-load nil
              :job-collapse-all       false
              :toggle-details-action  "expand"
              :job-display-max        42
              :job-sort               (nth utl/job-sorts 0)
              :show-filters           true
              :show-filtered-excluded false
              :rgx-match-case         false
              :rgx-xlate-or-and       true
              :search-history         {}
              :show-job-details       {}})))

(def job-sort (r/cursor app [:job-sort]))
(def job-collapse-all (r/cursor app [:job-collapse-all]))
(def toggle-details-action (r/cursor app [:toggle-details-action]))
(def job-display-max (r/cursor app [:job-display-max]))
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

