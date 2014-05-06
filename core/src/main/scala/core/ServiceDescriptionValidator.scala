package core

import play.api.libs.json.JsValue
import com.fasterxml.jackson.core.{ JsonParseException, JsonProcessingException }
import com.fasterxml.jackson.databind.JsonMappingException

case class ServiceDescriptionValidator(apiJson: String) {

  // TODO validate all type names are legal.
  // this includes types in
  //  - fields
  //  - operation argument lists
  //  - responses
  // Currently, illegal types are:
  //  - nested lists

  private val RequiredFields = Seq("base_url", "name", "resources")

  private var parseError: Option[String] = None

  lazy val serviceDescription: Option[ServiceDescription] = {
    try {
      Some(ServiceDescription(apiJson))
    } catch {
      case e: JsonParseException => {
        parseError = Some(e.getMessage)
        None
      }
      case e: JsonProcessingException => {
        parseError = Some(e.getMessage)
        None
      }
      case e: JsonMappingException => {
        parseError = Some(e.getMessage)
        None
      }
      case e: Throwable => throw e
    }
  }

  lazy val errors: Seq[String] = {
    serviceDescription match {

      case None => {
        if (apiJson == "") {
          Seq("No Data")
        } else {
          Seq(parseError.getOrElse("Invalid JSON"))
        }
      }

      case Some(sd: ServiceDescription) => {
        val requiredFieldErrors = validateRequiredFields()

        if (requiredFieldErrors.isEmpty) {
          validateResources ++ validateReferences ++ validateResponses
        } else {
          requiredFieldErrors
        }
      }
    }
  }

  def isValid: Boolean = {
    errors.isEmpty
  }


  /**
   * Validate basic structure, returning a list of error messages
   */
  private def validateRequiredFields(): Seq[String] = {
    val missing = RequiredFields.filter { field =>
      (serviceDescription.get.json \ field).asOpt[JsValue] match {
        case None => true
        case Some(_) => false
      }
    }
    if (missing.isEmpty) {
      Seq.empty
    } else {
      Seq("Missing: " + missing.mkString(", "))
    }
  }

  /**
   * Validate references to ensure they refer to proper data
   */
  private def validateReferences(): Seq[String] = {
    serviceDescription.get.resources.flatMap { resource =>
      resource.fields.flatMap { field =>
        field.references.flatMap { ref =>
          serviceDescription.get.resources.find { r => r.name == ref.resourceName } match {

            case None => Some(s"Resource ${resource.name} field ${field.name} reference ${ref.label} points to a non existent resource (${ref.resourceName})")

            case Some(r: Resource) => {
              r.fields.find(f => f.name == ref.field.name ) match {
                case None => Some(s"Resource ${resource.name} field ${field.name} reference ${ref.label} points to a non existent field (${ref.field})")
                case Some(f: Field) => None
              }
            }
          }
        }
      }
    }
  }

  private def validateResources(): Seq[String] = {
    if (serviceDescription.get.resources.isEmpty) {
      Seq("Must have at least one resource")
    } else {
      serviceDescription.get.resources.flatMap { resource =>
        resource.fields match {
          case Nil => Some(s"${resource.name} resource must have at least one field")
          case fields =>
            fields.collect {
              case field if field.default.nonEmpty =>
                s"Field ${field.name} of resource ${resource.name} should not have a default attribute. Default is only valid on an operation parameter."
            }
        }
      }
    }
  }

  private val NoContent = Set(204, 304)
  private def validateResponses(): Seq[String] = {
    serviceDescription.get.resources.flatMap { resource =>
      def path(op: Operation) = op.path match {
        case None => ""
        case Some(p: String) => s" $p"
      }
      val invalidResponses = resource.operations.flatMap { op =>
        op.responses.collect {
          case response if NoContent(response.code) =>
            if (response.dataType.name != Datatype.Unit.name) {
              Some(s"${resource.name} ${op.method}${path(op)} - response code ${response.code} must have a result type of[${Datatype.Unit.name}] and not[${response.dataType.name}]")
            } else {
              None
            }
          case _ @ Response(_, Datatype.UserType(_, _, _)) | Response(_, Datatype.List(_, Datatype.UserType(_, _, _))) => None // OK
          case _ =>
            Some(s"${resource.name} ${op.method}${path(op)} has invalid type. Must be either a resource or list of resources.")
        }
      }
      invalidResponses.flatten
    }
  }

}
