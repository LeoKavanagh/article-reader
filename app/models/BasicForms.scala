package models

import java.io.File

import play.api.data.Form
import play.api.data.Forms._

case class BasicForm(articleurl: String)

object BasicForm{
  val form: Form[BasicForm] = Form(
    mapping(
      "articleurl" -> text,
    )(BasicForm.apply)(BasicForm.unapply)
  )
}
