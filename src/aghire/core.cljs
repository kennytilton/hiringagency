(ns aghire.core
  (:require
    [reagent.core :as r]
    [aghire.utility :as utl]
    [aghire.sorting :as sort]
    [aghire.month-loader :as mld]
    [aghire.month-loader-ui :as mlv]

    [aghire.job-list :as jlst]
    [aghire.memo :as unt]
    [aghire.job-list-control-bar :as jlcb]
    [aghire.regex-ui :as rgxui]
    [aghire.filtering :as flt]))

(declare mount-root landing-page  app-banner)

;;; --- main ------------------------------------------------

(defn init! []
  (sort/sort-initialize)
  (unt/job-memos-load) ;; annotations of several kinds I make per story
  (mld/app-month-startup) ;; load the latest month be default

  (mount-root))

(defn mount-root []
  (r/render [landing-page] (.getElementById js/document "app")))

;; -------------------------
;; Landing-page

(defn landing-page []
  [:div
   [app-banner]
   [:div {:style {:margin 0 :background "#ffb57d"}}

    ;; we default to the current month and let the user explore
    ;; a couple of earlier months. Left as an exercise is loading
    ;; all the months and not restricting search by month boundary.
    [mlv/pick-a-month]

    ;; A hidden iframe used to load HN pages for scraping.
    ;; Left as an exercise is scraping once in a utility to
    ;; generate EDN used by this app -- except HN has an
    ;; API on the way that will elim scraping altogether!

    [mld/job-listing-loader]

    ;; the control panel...

    [:div {:style {:background "#ffb57d"}}
     [utl/open-shut-case :show-filters "Filters"
      flt/mk-title-selects
      flt/mk-user-selects]

     [rgxui/mk-regex-search]

     [sort/sort-bar]

     [jlcb/job-listing-control-bar]]

    ;; the jobs...
    [jlst/job-list]]])

;;; --- app help --------------------------------------------------------------
;;; We sneak in the banner here instead of breaking it out into its own file.

(def app-help-entry
  (map identity
    ["Click any job header to show or hide the full listing."
     "Double-click job description to open listing on HN in new tab."
     "All filters are ANDed except as you direct within RegExp fields."
     "Your edits are kept in local storage, so stick to one browser."
     "Works off page scrapes taken often enough. E-mail <a href='mailto:kentilton@gmail.com'>Kenny</a> if they seem stopped."
     "RFEs welcome and can be raised <a href='https://github.com/kennytilton/whoshiring/issues'>here</a>. "
     "Built with <a href='https://github.com/kennytilton/matrix/blob/master/js/matrix/readme.md'>Matrix Inside</a>&trade;."
     "This page is not affiliated with Hacker News, but..."
     "..thanks to the HN crew for their assistance. All screw-ups remain <a href='https://news.ycombinator.com/user?id=kennytilton'>kennytilton</a>'s."
     "Graphic design by <a href='https://www.mloboscoart.com'>Michael Lobosco</a>."]))

(defn app-banner []
  (let [helping (r/atom false)]
    (fn []
      [:div {:style {:background "PAPAYAWHIP"}}
       [:header
        [:div.about {
                     :title    "Usage hints, and credit where due."
                     :on-click #(swap! helping not)}
         "Pro Tips"]
        [:div.headermain
         [:span.askhn "Ask HN:"]
         [:span.who "Who Is Hiring?"]]]
       [utl/help-list app-help-entry helping]])))