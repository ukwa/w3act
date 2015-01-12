package controllers

import play.api.mvc.Action
import play.api.libs.ws.WS
import java.io.InputStream
import play.api.mvc.Controller
import play.api.libs.iteratee.Enumerator
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

object Helper extends Controller {

  def passthrough(url: String, relative: String) = Action.async {
    WS.url(url + relative).get().map { response =>
      val asStream: InputStream = response.underlying[com.ning.http.client.Response].getResponseBodyAsStream
      Ok.chunked(Enumerator.fromStream(asStream)).as("application/pdf").withHeaders(CONTENT_TRANSFER_ENCODING -> "binary")
    }
  }

}