(ns owlet-ui.views.settings
  (:require
    [owlet-ui.components.settings.activity-classifier :refer [activity-classsifer-component]]
    [owlet-ui.utils :refer [get-user-cms-profile]]
    [owlet-ui.config :as config]
    [reagent.validation :as validation]
    [reagent.session :as session]
    [reagent.core :as reagent :refer [atom]]
    [ajax.core :refer [PUT]]
    [re-frame.core :as re-frame]))

(def district-id (atom nil))

(defn settings-view []
  (let [sid (re-frame/subscribe [:social-id-subscription])]
    (reagent/create-class
      {:component-did-mount
       (fn []
         (get-user-cms-profile
           @sid
           #(reset! district-id (:district_id %))))
       :reagent-render
       (fn []
         [:div.settings
          [:h1#title [:mark "My Settings"]]
          [:div.container
           [:div.row
            [:div.col-lg-6
             [activity-classsifer-component]]
            [:div.col-lg-12
             [:div.search
              [:label "District ID:"
               [:input.test {:type        "text"
                             :value       @district-id
                             :placeholder 123456
                             :max-length  6
                             :on-change   #(reset! district-id (-> % .-target .-value))}]
               [:input {:type     "submit"
                        :value    "Enter"
                        :on-click #(when (and (validation/has-value? (session/get :user-id))
                                              (validation/has-value? @district-id))
                                     (PUT (str config/server-url "/api/users-district-id")
                                          {:params  {:district-id @district-id
                                                     :user-id     (session/get :user-id)}
                                           :handler (fn [res]
                                                      (js/alert res))}))}]]]]]]])})))
