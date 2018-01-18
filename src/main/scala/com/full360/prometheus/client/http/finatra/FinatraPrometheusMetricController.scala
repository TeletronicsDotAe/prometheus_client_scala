package com.full360.prometheus.client.http.finatra

import com.full360.prometheus.client.metric.Metric
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class FinatraPrometheusMetricController extends Controller {

  get("/prometheus") { _: Request ⇒ Metric.toString }
}
