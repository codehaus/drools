
(deftemplate guest (slot name) (slot sex) (slot hobby))

(deftemplate last_seat (slot seat))

(deftemplate seating
  (slot id) (slot pid) (slot seat1) (slot name1) (slot name2) (slot seat2) (slot path_done))

(deftemplate path (slot id) (slot name) (slot seat))

(deftemplate chosen (slot id) (slot name) (slot hobby))

(deftemplate count (slot c))

(deftemplate context (slot state))

(defrule assign_first_seat
  ?ctxt <- (context (state start))
  (guest (name ?n))
  =>
  (assert (seating (seat1 1) (name1 ?n) (name2 ?n) (seat2 1) (id 1) (pid 0) (path_done yes)))
  (assert (path (id 1) (name ?n) (seat 1)))
  (assert (count (c 2)))
  (printout t seat " " 1 " " ?n " " ?n " " 1 " " 1 " " 0 " " 1 crlf)
  (modify ?ctxt (state assign_seats)))

(defrule find_seating
  ?ctxt <- (context (state assign_seats))
  (seating (seat1 ?seat1) (seat2 ?seat2) (name2 ?n2) (id ?id) (pid ?pid) (path_done yes))
  (guest (name ?n2) (sex ?s1) (hobby ?h1))
  (guest (name ?g2) (sex ~?s1) (hobby ?h1))
  ?count <- (count (c ?c))
  (not (path (id ?id) (name ?g2)))
  (not (chosen (id ?id) (name ?g2) (hobby ?h1)))
  =>
  (assert (seating (seat1 ?seat2) (name1 ?n2) (name2 ?g2) (seat2 (+ ?seat2 1)) (id ?c)
      (pid ?id) (path_done no)))
  (assert (path (id ?c) (name ?g2) (seat ( + ?seat2 1))))
  (assert (chosen (id ?id) (name ?g2) (hobby ?h1)))
  (modify ?count (c (+ ?c 1)))
  (printout t seat " " ?seat2 " " ?n2 " " ?g2 crlf)
  (modify ?ctxt (state make_path)))

(defrule path_done
  ?ctxt <- (context (state make_path))
  ?seat <- (seating (path_done no))
  =>
  (modify ?seat (path_done yes))
  (modify ?ctxt (state check_done)))

(defrule make_path
  (context (state make_path))
  (seating (id ?id) (pid ?pid) (path_done no))
  (path (id ?pid)(name ?n1) (seat ?s))
  (not (path (id ?id) (name ?n1)))
  =>
  (assert (path (id ?id) (name ?n1) (seat ?s))))

(defrule continue
  ?ctxt <- (context (state check_done))
  =>
  (modify ?ctxt (state assign_seats)))

(defrule are_we_done
  ?ctxt <- (context (state check_done))
  (last_seat (seat ?l_seat))
  (seating (seat2 ?l_seat))
  ?count <- (count (c ?c))
  =>
  (printout t crlf "Yes, we are done!!" crlf)
  (modify ?count (c 1))
  (modify ?ctxt (state print_results)))

(defrule print_results
  (context (state print_results))
  (seating (id ?id) (seat2 ?s2))
  (last_seat (seat ?s2))
  ?count <- (count (c ?c))
  ?path <- (path (id ?id) (name ?n) (seat ?c))
  =>
  (modify ?count (c (+ ?c 1)))
  (printout t ?c " " ?n crlf))

(set-reset-globals t)
(defglobal ?*t* = (time))

(reset)
(load-facts ../manners64.dat)

;(watch facts)
;(watch rules)

(run)
(printout t "Elapsed time: " (- (time) ?*t*) crlf)
(exit)
