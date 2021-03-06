package core

import com.gilt.apidocgenerator.models.{Container, Type, TypeKind, TypeInstance}
import org.scalatest.{FunSpec, Matchers}

class ServiceDescriptionInternalReferencesSpec extends FunSpec with Matchers {

  it("Validates circular reference") {
    val json = """
    {
      "base_url": "http://localhost:9000",
      "name": "svc-circular-reference",
      "models": {
        "foo": {
            "fields": [
                { "name": "bar", "type": "bar" }
            ]
        },
        "bar": {
            "fields": [
                { "name": "foo", "type": "foo" }
            ]
        }
      }
    }
    """

    val validator = ServiceDescriptionValidator(json)
    validator.errors.mkString("") should be("")

    val barField = validator.serviceDescription.get.models.find(_.name == "foo").get.fields.find(_.name == "bar").get
    barField.`type` should be(TypeInstance(Container.Singleton, Type(TypeKind.Model, "bar")))

    val fooField = validator.serviceDescription.get.models.find(_.name == "bar").get.fields.find(_.name == "foo").get
    fooField.`type` should be(TypeInstance(Container.Singleton, Type(TypeKind.Model, "foo")))
  }

  it("Is able to parse self reference") {
    val json = """
    {
      "base_url": "http://localhost:9000",
      "name": "svc-reference",
      "models": {
        "userVariant": {
            "description": "variant set a user belongs to for a particular test.",
            "fields": [
                {
                    "name": "variantKey",
                    "type": "string"
                },
                {
                    "name": "parent",
                    "type": "userVariant",
                    "required": "false"
                }
            ]
        }
      }
    }
    """

    val validator = ServiceDescriptionValidator(json)
    validator.errors.mkString("") should be("")
    val model = validator.serviceDescription.get.models.find(_.name == "userVariant").get
    val parentField = model.fields.find(_.name == "parent").get
    parentField.`type` should be(TypeInstance(Container.Singleton, Type(TypeKind.Model, "userVariant")))
  }

}
