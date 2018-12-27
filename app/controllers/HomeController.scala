package controllers

import javax.inject.Inject
import javax.inject.Singleton

import models.BasicForm
import play.api.libs.Files
import play.api.mvc._

import controllers.ArticleReader.process

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index() = Action { 
    implicit request: Request[AnyContent] => Redirect("/simpleForm")
  }

  def simpleForm() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.basicForm(BasicForm.form))
  }

  def simpleFormPost() = Action { implicit request =>
    BasicForm.form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.basicForm(formWithErrors))
      },
      formData => {
        process(formData.articleurl.toString)
        Ok(views.html.reader())
      }
    )
  }
}
