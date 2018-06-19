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

;(rfr/reg-event-fx ::initialize-db
;  [(rfr/inject-cofx :storage-user-notes)]
;
;  (fn [{:keys [storage-user-notes]} _]
;    (merge
;      {:db (assoc (initial-db) :user-notes storage-user-notes)}
;      (when-let [initial-month (nth (loader/gMonthlies-cljs) js/initialSearchMoIdx)]
;        {:dispatch [:month-set (:hnId initial-month)]}))))

