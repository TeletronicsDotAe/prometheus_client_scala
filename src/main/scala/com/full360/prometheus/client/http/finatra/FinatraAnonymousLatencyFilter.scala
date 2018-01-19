package com.full360.prometheus.client.http.finatra

import com.full360.prometheus.client.http.HttpLatency
import com.full360.prometheus.client.util.Timer
import com.twitter.finagle.{ Service, SimpleFilter }
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.util.Future

class FinatraAnonymousLatencyFilter extends SimpleFilter[Request, Response] with HttpLatency {

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    val timer = new Timer

    service(request)
      .onSuccess(response ⇒ register(timer, request, Some(response)))
      .onFailure(_ ⇒ register(timer, request, None))
  }

  def register(timer: Timer, request: Request, response: Option[Response] = None) = super.register(
    timer.stop,
    request.method.toString().toLowerCase,
    request.host.getOrElse("unknown"),
    // request.path may contain ids (when implementing the http rest api in http routes), so consider looking at
    // https://github.com/full360/prometheus_client_scala/blob/master/client-finatra/src/main/scala/com/full360/prometheus/http/finatra/Finatra.scala
    request.path,
    response.map(_.getStatusCode()).getOrElse(500)
  )
}
