;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.data.persistence
  (:require
   [app.common.data :as d]
   [app.common.data.macros :as dm]
   [app.common.logging :as log]
   [app.common.pages :as cp]
   [app.common.pages.changes :as cpc]
   [app.common.types.shape-tree :as ctst]
   [app.common.uuid :as uuid]
   [app.main.data.workspace.changes :as dch]
   [app.main.data.workspace.thumbnails :as dwt]
   [app.main.features :as features]
   [app.main.repo :as rp]
   [app.main.store :as st]
   [app.util.router :as rt]
   [app.util.time :as dt]
   [beicon.core :as rx]
   [okulary.core :as l]
   [potok.core :as ptk]))

(log/set-level! :trace)

(def running (atom false))
(def revn-data (atom {}))
(def queue-conj (fnil conj #queue []))

(defn- discard-commit
  [commit-id]
  (ptk/reify ::discard-commit
    ptk/UpdateEvent
    (update [_ state]
      (update-in state [:persistence :queue]
                 (fn [queue]
                   (if (= commit-id (peek queue))
                     (pop queue)
                     (throw (ex-info "invalid state" {}))))))))

(defn persist-commit
  [commit-id]
  (ptk/reify ::persist-commit
    ptk/WatchEvent
    (watch [_ state stream]
      (when-let [{:keys [file-id file-revn changes] :as commit} (dm/get-in state [:persistence :index commit-id])]
        (let [;; this features set does not includes the ffeat/enabled
              ;; because they are already available on the backend and
              ;; this request provides a set of features to enable in
              ;; this request.
              features (cond-> #{}
                         (features/active-feature? state :components-v2)
                         (conj "components/v2"))
              sid      (:session-id state)
              revn     (max file-revn (get @revn-data file-id 0))
              params   {:id file-id
                        :revn revn
                        :session-id sid
                        :origin (:origin commit)
                        :created-at (:created-at commit)
                        :commit-id commit-id
                        :changes (vec changes)
                        :features features}]

          (->> (rp/cmd! :update-file params)
               (rx/mapcat (fn [{:keys [revn lagged] :as response}]
                            (log/debug :hint "changes persisted" :commit-id commit-id :lagged (count lagged))
                            (swap! revn-data update file-id (fnil max revn) (:revn response))

                            ;; FIXME: handle lagged here
                            (rx/of (ptk/data-event ::commit-persisted commit)
                                   #_(ptk/data-event ::file-revn-updated {:file-id file-id :revn revn}))))

               (rx/catch (fn [cause]
                           (rx/concat
                            (if (= :authentication (:type cause))
                              (rx/empty)
                              (rx/of (rt/assign-exception cause)))
                            (rx/throw cause))))))))))


;; #_(if (seq lagged)
;;   (rx/of (apply-changes-locally file-id lagged))
;;   (rx/empty))

;; #_(rx/merge
;;  (->> (rx/from (concat lagged commits))
;;       (rx/merge-map
;;        (fn [{:keys [changes] :as entry}]
;;          (rx/merge
;;           (rx/from
;;            (for [[page-id changes] (group-by :page-id changes)]
;;              (dch/update-indices page-id changes)))
;;           (rx/of (shapes-changes-persisted file-id entry)))))))


(defn- run-persistence
  []
  (ptk/reify ::run-persistence
    ptk/WatchEvent
    (watch [_ state stream]
      (let [{:keys [queue index]} (:persistence state)]
        (if-let [commit-id (peek queue)]
          (->> (rx/merge
                (rx/of (persist-commit commit-id))
                (->> stream
                     (rx/filter (ptk/type? ::commit-persisted))
                     (rx/map deref)
                     (rx/filter #(= commit-id (:id %)))
                     (rx/take 1)
                     (rx/mapcat (fn [_]
                                  (rx/of (discard-commit commit-id)
                                         (run-persistence))))))
               (rx/take-until
                (rx/filter (ptk/type? ::run-persistence) stream)))
          (do
            (reset! running false)
            nil))))))

(defn- append-commit
  "Event used internally to append the current change to the
  persistence queue."
  [{:keys [id] :as commit}]
  (ptk/reify ::append-commit
    ptk/UpdateEvent
    (update [_ state]
      (update state :persistence
              (fn [state]
                (-> state
                    (update :queue queue-conj id)
                    (update :index assoc id commit)))))

    ptk/WatchEvent
    (watch [_ state stream]
      (when (compare-and-set! running false true)
        (rx/of (run-persistence))))))


(defn initialize-persistence
  []
  (ptk/reify ::initialize-persistence
    ptk/WatchEvent
    (watch [_ _ stream]
      (log/debug :hint "initialize persistence")
      (let [stoper (rx/filter (ptk/type? ::initialize-persistence) stream)]
        (->> stream
             (rx/filter dch/commit-changes?)
             (rx/map deref)
             (rx/filter (complement empty?))
             (rx/map append-commit)
             (rx/take-until (rx/delay 100 stoper))
             (rx/finalize (fn []
                            (log/debug :hint "finalize persistence: changes watcher"))))))))




;; (defn apply-changes-locally
;;   [file-id {:keys [revn changes]}]
;;   (dm/assert! (uuid? file-id))
;;   (dm/assert! (int? revn))
;;   (dm/assert! (cpc/valid-changes? changes))

;;   #_(ptk/reify ::applly-changes-locally
;;     ptk/UpdateEvent
;;     (update [_ state]
;;       ;; NOTE: we don't set the file features context here because
;;       ;; there are no useful context for code that need to be executed
;;       ;; on the frontend side

;;       (if-let [current-file-id (:current-file-id state)]
;;         (if (= file-id current-file-id)
;;           (let [changes (group-by :page-id changes)]
;;             (-> state
;;                 (update-in [:workspace-file :revn] max revn)
;;                 (update :workspace-data (fn [file]
;;                                           (loop [fdata file
;;                                                  entries (seq changes)]
;;                                             (if-let [[page-id changes] (first entries)]
;;                                               (recur (-> fdata
;;                                                          (cp/process-changes changes)
;;                                                          (ctst/update-object-indices page-id))
;;                                                      (rest entries))
;;                                               fdata))))))
;;           (-> state
;;               (d/update-in-when [:workspace-libraries file-id :revn] max revn)
;;               (d/update-in-when [:workspace-libraries file-id :data] cp/process-changes changes)))

;;         state))

;;     ptk/WatchEvent
;;     (watch [_ state stream]
;;       (->> (rx/from lagged)
;;            (rx/merge-map
;;             (fn [{:keys [changes] :as entry}]
;;               (rx/merge
;;                (rx/from
;;                 (for [[page-id changes] (group-by :page-id changes)]
;;                   (dch/update-indices page-id changes)))
;;                (rx/of (shapes-changes-persisted file-id entry)))))))))

