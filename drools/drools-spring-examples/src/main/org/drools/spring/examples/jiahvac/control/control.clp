;; Load our Userfunctions
(load-package control.HardwareFunctions)

;; Create a simulator
(defglobal ?*hw* = (init-simulator 33))

(defglobal ?*set-point* = 70)

;; Tell Jess about the Java Bean classes
(defclass Thermometer control.Thermometer)
(defclass Vent control.Vent)
(defclass HeatPump control.HeatPump)

;; Create the Vent and Thermometer Beans
(bind ?n (n-floors))
(while (> ?n 0) do
       (definstance Thermometer (new control.Thermometer ?*hw* ?n))
       (definstance Vent (new control.Vent ?*hw* ?n))
       (bind ?n (- ?n 1)))

;; Create the HeatPump Beans
(bind ?n (n-heatpumps))
(while (> ?n 0) do
       (definstance HeatPump (new control.HeatPump ?*hw* ?n))
       (bind ?n (- ?n 1)))

;; Deffunctions
(deffunction too-cold (?t)
  (return (< ?t (- ?*set-point* 2))))

(deffunction too-hot (?t)
  (return (> ?t (+ ?*set-point* 2))))

;; Heat pump rules
(defrule floor-too-cold-pump-off
  (HeatPump (state "off") (number ?p))
  (Thermometer (floor ?f&:(eq ?p (which-pump ?f)))
               (reading ?t&:(too-cold ?t)))
  =>
  (set-hp-state ?p heating))

(defrule floors-hot-enough
  (HeatPump (state "heating") (number ?p))
  (not (Thermometer (floor ?f&:(eq ?p (which-pump ?f)))
                    (reading ?t&:(< ?t ?*set-point*))))
  =>
  (set-hp-state ?p off))

(defrule floor-too-hot-pump-off
  (HeatPump (state "off") (number ?p))
  (Thermometer (floor ?f&:(eq ?p (which-pump ?f)))
               (reading ?t&:(too-hot ?t)))
  =>
  (set-hp-state ?p cooling))

(defrule floors-cool-enough
  (HeatPump (state "cooling") (number ?p))
  (not (Thermometer (floor ?f&:(eq ?p (which-pump ?f)))
                    (reading ?t&:(> ?t ?*set-point*))))
  =>
  (set-hp-state ?p off))

(defrule floor-too-cold-vent-closed
  (HeatPump (state "heating") (number ?p))
  (Vent (state "closed") (floor ?f&:(eq ?p (which-pump ?f))))
  (Thermometer (floor ?f)
               (reading ?t&:(< ?t ?*set-point*)))
  =>
  (set-vent-state ?f open))

(defrule floor-too-cold-vent-open
  (HeatPump (state "cooling") (number ?p))
  (Vent (state "open") (floor ?f&:(eq ?p (which-pump ?f))))
  (Thermometer (floor ?f)
               (reading ?t&:(< ?t ?*set-point*)))
  =>
  (set-vent-state ?f closed))

(defrule floor-too-hot-vent-closed
  (HeatPump (state "cooling") (number ?p))
  (Vent (state "closed") (floor ?f&:(eq ?p (which-pump ?f))))
  (Thermometer (floor ?f)
               (reading ?t&:(> ?t ?*set-point*)))
  =>
  (set-vent-state ?f open))

(defrule floor-too-hot-vent-open
  (HeatPump (state "heating") (number ?p))
  (Vent (state "open") (floor ?f&:(eq ?p (which-pump ?f))))
  (Thermometer (floor ?f)
               (reading ?t&:(> ?t ?*set-point*)))
  =>
  (set-vent-state ?f closed))

(watch rules)
(run-until-halt)


