;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.auth
  (:require-macros [app.main.style :as stl])
  (:require
   [app.config :as cf]
   [app.main.ui.auth.login :refer [login-page]]
   [app.main.ui.auth.recovery :refer [recovery-page]]
   [app.main.ui.auth.recovery-request :refer [recovery-request-page]]
   [app.main.ui.auth.register :refer [register-page register-success-page register-validate-page]]
   [app.main.ui.context :as ctx]
   [app.main.ui.icons :as i]
   [app.util.dom :as dom]
   [app.util.i18n :as i18n :refer [tr]]
   [rumext.v2 :as mf]))

(mf/defc terms-login
  []
  (let [new-css-system (mf/use-ctx ctx/new-css-system)
        show-all?     (and cf/terms-of-service-uri cf/privacy-policy-uri)
        show-terms?   (some? cf/terms-of-service-uri)
        show-privacy? (some? cf/privacy-policy-uri)]

    (if new-css-system
      (when show-all?
        [:div {:class (stl/css :terms-login)}
         (when show-terms?
           [:a {:href cf/terms-of-service-uri :target "_blank"} (tr "auth.terms-of-service")])

         (when show-all?
           [:span (tr "labels.and")])

         (when show-privacy?
           [:a {:href cf/privacy-policy-uri :target "_blank"} (tr "auth.privacy-policy")])])

      (when show-all?
        [:div.terms-login
         (when show-terms?
           [:a {:href cf/terms-of-service-uri :target "_blank"} (tr "auth.terms-of-service")])

         (when show-all?
           [:span (tr "labels.and")])

         (when show-privacy?
           [:a {:href cf/privacy-policy-uri :target "_blank"} (tr "auth.privacy-policy")])]))))

(mf/defc auth
  [{:keys [route] :as props}]
  (let [new-css-system (mf/use-ctx ctx/new-css-system)
        section (get-in route [:data :name])
        params  (:query-params route)
        show-illustration? true]

    (mf/use-effect
     #(dom/set-html-title (tr "title.default")))

    (if new-css-system
      [:main {:class (stl/css-case :auth-section true
                                   :no-ilustration (not show-illustration?))}
       (when show-illustration?
         [:div {:class (stl/css :login-illustration)}
          i/login-ilustration])
       [:section {:class (stl/css :auth-content)}
        (case section
          :auth-register
          [:& register-page {:params params}]

          :auth-register-validate
          [:& register-validate-page {:params params}]

          :auth-register-success
          [:& register-success-page {:params params}]

          :auth-login
          [:& login-page {:params params}]

          :auth-recovery-request
          [:& recovery-request-page]

          :auth-recovery
          [:& recovery-page {:params params}])

        (when (contains? #{:auth-login :auth-register} section)
          [:& terms-login])]]

      ;; OLD
      [:main.auth
       [:section.auth-sidebar
        [:a.logo {:href "#/"}
         [:span {:aria-hidden true} i/logo]
         [:span.hidden-name "Home"]]
        [:span.tagline (tr "auth.sidebar-tagline")]]

       [:section.auth-content
        (case section
          :auth-register
          [:& register-page {:params params}]

          :auth-register-validate
          [:& register-validate-page {:params params}]

          :auth-register-success
          [:& register-success-page {:params params}]

          :auth-login
          [:& login-page {:params params}]

          :auth-recovery-request
          [:& recovery-request-page]

          :auth-recovery
          [:& recovery-page {:params params}])

        [:& terms-login {}]]])))
