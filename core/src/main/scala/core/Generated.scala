package com.gilt.apidoc.models {

  /**
   * Generated source code.
   */
  case class Code(
    generator: com.gilt.apidoc.models.Generator,
    source: String
  )

  /**
   * Represents a single domain name (e.g. www.apidoc.me). When a new user registers
   * and confirms their email, we automatically associate that user with a member of
   * the organization associated with their domain. For example, if you confirm your
   * account with an email address of foo@gilt.com, we will automatically create a
   * membership request on your behalf to join the organization with domain gilt.com.
   */
  case class Domain(
    name: String
  )

  case class Error(
    code: String,
    message: String
  )

  /**
   * An apidoc generator
   */
  case class Generator(
    guid: _root_.java.util.UUID,
    key: String,
    uri: String,
    name: String,
    language: scala.Option[String] = None,
    description: scala.Option[String] = None,
    visibility: com.gilt.apidoc.models.Visibility,
    owner: com.gilt.apidoc.models.User,
    enabled: Boolean
  )

  /**
   * Form to create a new generator
   */
  case class GeneratorCreateForm(
    key: String,
    uri: String,
    visibility: com.gilt.apidoc.models.Visibility
  )

  /**
   * Form to enable or disable a generator for an organization
   */
  case class GeneratorOrgForm(
    enabled: Boolean
  )

  /**
   * Form to update a generator
   */
  case class GeneratorUpdateForm(
    visibility: scala.Option[com.gilt.apidoc.models.Visibility] = None,
    enabled: scala.Option[Boolean] = None
  )

  case class Healthcheck(
    status: String
  )

  /**
   * A membership represents a user in a specific role to an organization.
   * Memberships cannot be created directly. Instead you first create a membership
   * request, then that request is either accepted or declined.
   */
  case class Membership(
    guid: _root_.java.util.UUID,
    user: com.gilt.apidoc.models.User,
    organization: com.gilt.apidoc.models.Organization,
    role: String
  )

  /**
   * A membership request represents a user requesting to join an organization with a
   * specific role (e.g. as a member or an admin). Membership requests can be
   * reviewed by any current admin of the organization who can either accept or
   * decline the request.
   */
  case class MembershipRequest(
    guid: _root_.java.util.UUID,
    user: com.gilt.apidoc.models.User,
    organization: com.gilt.apidoc.models.Organization,
    role: String
  )

  /**
   * An organization is used to group a set of services together.
   */
  case class Organization(
    guid: _root_.java.util.UUID,
    key: String,
    name: String,
    domains: Seq[com.gilt.apidoc.models.Domain] = Nil,
    metadata: scala.Option[com.gilt.apidoc.models.OrganizationMetadata] = None
  )

  case class OrganizationForm(
    name: String,
    key: scala.Option[String] = None,
    domains: Seq[String] = Nil,
    metadata: scala.Option[com.gilt.apidoc.models.OrganizationMetadataForm] = None
  )

  /**
   * Supplemental (non-required) information about an organization
   */
  case class OrganizationMetadata(
    visibility: scala.Option[com.gilt.apidoc.models.Visibility] = None,
    packageName: scala.Option[String] = None
  )

  case class OrganizationMetadataForm(
    visibility: scala.Option[com.gilt.apidoc.models.Visibility] = None,
    packageName: scala.Option[String] = None
  )

  /**
   * A service has a name and multiple versions of an API (Interface).
   */
  case class Service(
    guid: _root_.java.util.UUID,
    name: String,
    key: String,
    visibility: com.gilt.apidoc.models.Visibility,
    description: scala.Option[String] = None
  )

  /**
   * Represents a user that is currently subscribed to a publication
   */
  case class Subscription(
    guid: _root_.java.util.UUID,
    organization: com.gilt.apidoc.models.Organization,
    user: com.gilt.apidoc.models.User,
    publication: com.gilt.apidoc.models.Publication
  )

  case class SubscriptionForm(
    organizationKey: String,
    userGuid: _root_.java.util.UUID,
    publication: com.gilt.apidoc.models.Publication
  )

  /**
   * A token gives a user access to the API.
   */
  case class Token(
    guid: _root_.java.util.UUID,
    user: com.gilt.apidoc.models.User,
    token: String,
    description: scala.Option[String] = None
  )

  case class TokenForm(
    userGuid: _root_.java.util.UUID,
    description: scala.Option[String] = None
  )

  /**
   * A user is a top level person interacting with the api doc server.
   */
  case class User(
    guid: _root_.java.util.UUID,
    email: String,
    name: scala.Option[String] = None
  )

  /**
   * Used only to validate json files - used as a resource where http status code
   * defines success
   */
  case class Validation(
    valid: Boolean,
    errors: Seq[String] = Nil
  )

  /**
   * Represents a unique version of the service.
   */
  case class Version(
    guid: _root_.java.util.UUID,
    version: String,
    json: String
  )

  /**
   * Users can watch individual services which enables features like receiving an
   * email notification when there is a new version of a service.
   */
  case class Watch(
    guid: _root_.java.util.UUID,
    user: com.gilt.apidoc.models.User,
    organization: com.gilt.apidoc.models.Organization,
    service: com.gilt.apidoc.models.Service
  )

  case class WatchForm(
    userGuid: _root_.java.util.UUID,
    organizationKey: String,
    serviceKey: String
  )

  /**
   * A publication represents something that a user can subscribe to. An example
   * would be subscribing to an email alert whenever a new version of a service is
   * created.
   */
  sealed trait Publication

  object Publication {

    /**
     * For organizations for which I am an administrator, email me whenever a user
     * applies to join the org.
     */
    case object MembershipRequestsCreate extends Publication { override def toString = "membership_requests.create" }
    /**
     * For organizations for which I am a member, email me whenever a user join the
     * org.
     */
    case object MembershipsCreate extends Publication { override def toString = "memberships.create" }
    /**
     * For organizations for which I am a member, email me whenever a service is
     * created.
     */
    case object ServicesCreate extends Publication { override def toString = "services.create" }
    /**
     * For services that I watch, email me whenever a version is created.
     */
    case object VersionsCreate extends Publication { override def toString = "versions.create" }

    /**
     * UNDEFINED captures values that are sent either in error or
     * that were added by the server after this library was
     * generated. We want to make it easy and obvious for users of
     * this library to handle this case gracefully.
     *
     * We use all CAPS for the variable name to avoid collisions
     * with the camel cased values above.
     */
    case class UNDEFINED(override val toString: String) extends Publication

    /**
     * all returns a list of all the valid, known values. We use
     * lower case to avoid collisions with the camel cased values
     * above.
     */
    val all = Seq(MembershipRequestsCreate, MembershipsCreate, ServicesCreate, VersionsCreate)

    private[this]
    val byName = all.map(x => x.toString -> x).toMap

    def apply(value: String): Publication = fromString(value).getOrElse(UNDEFINED(value))

    def fromString(value: String): scala.Option[Publication] = byName.get(value)

  }

  /**
   * Controls who is able to view this version
   */
  sealed trait Visibility

  object Visibility {

    /**
     * Only the creator can view this service
     */
    case object User extends Visibility { override def toString = "user" }
    /**
     * Any member of the organization can view this service
     */
    case object Organization extends Visibility { override def toString = "organization" }
    /**
     * Anybody, including non logged in users, can view this service
     */
    case object Public extends Visibility { override def toString = "public" }

    /**
     * UNDEFINED captures values that are sent either in error or
     * that were added by the server after this library was
     * generated. We want to make it easy and obvious for users of
     * this library to handle this case gracefully.
     *
     * We use all CAPS for the variable name to avoid collisions
     * with the camel cased values above.
     */
    case class UNDEFINED(override val toString: String) extends Visibility

    /**
     * all returns a list of all the valid, known values. We use
     * lower case to avoid collisions with the camel cased values
     * above.
     */
    val all = Seq(User, Organization, Public)

    private[this]
    val byName = all.map(x => x.toString -> x).toMap

    def apply(value: String): Visibility = fromString(value).getOrElse(UNDEFINED(value))

    def fromString(value: String): scala.Option[Visibility] = byName.get(value)

  }

}

package com.gilt.apidoc.models {
  package object json {
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._

    private[apidoc] implicit val jsonReadsUUID = __.read[String].map(java.util.UUID.fromString)

    private[apidoc] implicit val jsonWritesUUID = new Writes[java.util.UUID] {
      def writes(x: java.util.UUID) = JsString(x.toString)
    }

    private[apidoc] implicit val jsonReadsJodaDateTime = __.read[String].map { str =>
      import org.joda.time.format.ISODateTimeFormat.dateTimeParser
      dateTimeParser.parseDateTime(str)
    }

    private[apidoc] implicit val jsonWritesJodaDateTime = new Writes[org.joda.time.DateTime] {
      def writes(x: org.joda.time.DateTime) = {
        import org.joda.time.format.ISODateTimeFormat.dateTime
        val str = dateTime.print(x)
        JsString(str)
      }
    }

    implicit val jsonReadsApidocEnum_Publication = __.read[String].map(Publication.apply)
    implicit val jsonWritesApidocEnum_Publication = new Writes[Publication] {
      def writes(x: Publication) = JsString(x.toString)
    }

    implicit val jsonReadsApidocEnum_Visibility = __.read[String].map(Visibility.apply)
    implicit val jsonWritesApidocEnum_Visibility = new Writes[Visibility] {
      def writes(x: Visibility) = JsString(x.toString)
    }
    implicit def jsonReadsApidocCode: play.api.libs.json.Reads[Code] = {
      (
        (__ \ "generator").read[com.gilt.apidoc.models.Generator] and
        (__ \ "source").read[String]
      )(Code.apply _)
    }

    implicit def jsonWritesApidocCode: play.api.libs.json.Writes[Code] = {
      (
        (__ \ "generator").write[com.gilt.apidoc.models.Generator] and
        (__ \ "source").write[String]
      )(unlift(Code.unapply _))
    }

    implicit def jsonReadsApidocDomain: play.api.libs.json.Reads[Domain] = {
      (__ \ "name").read[String].map { x => new Domain(name = x) }
    }

    implicit def jsonWritesApidocDomain: play.api.libs.json.Writes[Domain] = new play.api.libs.json.Writes[Domain] {
      def writes(x: Domain) = play.api.libs.json.Json.obj(
        "name" -> play.api.libs.json.Json.toJson(x.name)
      )
    }

    implicit def jsonReadsApidocError: play.api.libs.json.Reads[Error] = {
      (
        (__ \ "code").read[String] and
        (__ \ "message").read[String]
      )(Error.apply _)
    }

    implicit def jsonWritesApidocError: play.api.libs.json.Writes[Error] = {
      (
        (__ \ "code").write[String] and
        (__ \ "message").write[String]
      )(unlift(Error.unapply _))
    }

    implicit def jsonReadsApidocGenerator: play.api.libs.json.Reads[Generator] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "key").read[String] and
        (__ \ "uri").read[String] and
        (__ \ "name").read[String] and
        (__ \ "language").readNullable[String] and
        (__ \ "description").readNullable[String] and
        (__ \ "visibility").read[com.gilt.apidoc.models.Visibility] and
        (__ \ "owner").read[com.gilt.apidoc.models.User] and
        (__ \ "enabled").read[Boolean]
      )(Generator.apply _)
    }

    implicit def jsonWritesApidocGenerator: play.api.libs.json.Writes[Generator] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "key").write[String] and
        (__ \ "uri").write[String] and
        (__ \ "name").write[String] and
        (__ \ "language").write[scala.Option[String]] and
        (__ \ "description").write[scala.Option[String]] and
        (__ \ "visibility").write[com.gilt.apidoc.models.Visibility] and
        (__ \ "owner").write[com.gilt.apidoc.models.User] and
        (__ \ "enabled").write[Boolean]
      )(unlift(Generator.unapply _))
    }

    implicit def jsonReadsApidocGeneratorCreateForm: play.api.libs.json.Reads[GeneratorCreateForm] = {
      (
        (__ \ "key").read[String] and
        (__ \ "uri").read[String] and
        (__ \ "visibility").read[com.gilt.apidoc.models.Visibility]
      )(GeneratorCreateForm.apply _)
    }

    implicit def jsonWritesApidocGeneratorCreateForm: play.api.libs.json.Writes[GeneratorCreateForm] = {
      (
        (__ \ "key").write[String] and
        (__ \ "uri").write[String] and
        (__ \ "visibility").write[com.gilt.apidoc.models.Visibility]
      )(unlift(GeneratorCreateForm.unapply _))
    }

    implicit def jsonReadsApidocGeneratorOrgForm: play.api.libs.json.Reads[GeneratorOrgForm] = {
      (__ \ "enabled").read[Boolean].map { x => new GeneratorOrgForm(enabled = x) }
    }

    implicit def jsonWritesApidocGeneratorOrgForm: play.api.libs.json.Writes[GeneratorOrgForm] = new play.api.libs.json.Writes[GeneratorOrgForm] {
      def writes(x: GeneratorOrgForm) = play.api.libs.json.Json.obj(
        "enabled" -> play.api.libs.json.Json.toJson(x.enabled)
      )
    }

    implicit def jsonReadsApidocGeneratorUpdateForm: play.api.libs.json.Reads[GeneratorUpdateForm] = {
      (
        (__ \ "visibility").readNullable[com.gilt.apidoc.models.Visibility] and
        (__ \ "enabled").readNullable[Boolean]
      )(GeneratorUpdateForm.apply _)
    }

    implicit def jsonWritesApidocGeneratorUpdateForm: play.api.libs.json.Writes[GeneratorUpdateForm] = {
      (
        (__ \ "visibility").write[scala.Option[com.gilt.apidoc.models.Visibility]] and
        (__ \ "enabled").write[scala.Option[Boolean]]
      )(unlift(GeneratorUpdateForm.unapply _))
    }

    implicit def jsonReadsApidocHealthcheck: play.api.libs.json.Reads[Healthcheck] = {
      (__ \ "status").read[String].map { x => new Healthcheck(status = x) }
    }

    implicit def jsonWritesApidocHealthcheck: play.api.libs.json.Writes[Healthcheck] = new play.api.libs.json.Writes[Healthcheck] {
      def writes(x: Healthcheck) = play.api.libs.json.Json.obj(
        "status" -> play.api.libs.json.Json.toJson(x.status)
      )
    }

    implicit def jsonReadsApidocMembership: play.api.libs.json.Reads[Membership] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "user").read[com.gilt.apidoc.models.User] and
        (__ \ "organization").read[com.gilt.apidoc.models.Organization] and
        (__ \ "role").read[String]
      )(Membership.apply _)
    }

    implicit def jsonWritesApidocMembership: play.api.libs.json.Writes[Membership] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "user").write[com.gilt.apidoc.models.User] and
        (__ \ "organization").write[com.gilt.apidoc.models.Organization] and
        (__ \ "role").write[String]
      )(unlift(Membership.unapply _))
    }

    implicit def jsonReadsApidocMembershipRequest: play.api.libs.json.Reads[MembershipRequest] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "user").read[com.gilt.apidoc.models.User] and
        (__ \ "organization").read[com.gilt.apidoc.models.Organization] and
        (__ \ "role").read[String]
      )(MembershipRequest.apply _)
    }

    implicit def jsonWritesApidocMembershipRequest: play.api.libs.json.Writes[MembershipRequest] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "user").write[com.gilt.apidoc.models.User] and
        (__ \ "organization").write[com.gilt.apidoc.models.Organization] and
        (__ \ "role").write[String]
      )(unlift(MembershipRequest.unapply _))
    }

    implicit def jsonReadsApidocOrganization: play.api.libs.json.Reads[Organization] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "key").read[String] and
        (__ \ "name").read[String] and
        (__ \ "domains").readNullable[Seq[com.gilt.apidoc.models.Domain]].map(_.getOrElse(Nil)) and
        (__ \ "metadata").readNullable[com.gilt.apidoc.models.OrganizationMetadata]
      )(Organization.apply _)
    }

    implicit def jsonWritesApidocOrganization: play.api.libs.json.Writes[Organization] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "key").write[String] and
        (__ \ "name").write[String] and
        (__ \ "domains").write[Seq[com.gilt.apidoc.models.Domain]] and
        (__ \ "metadata").write[scala.Option[com.gilt.apidoc.models.OrganizationMetadata]]
      )(unlift(Organization.unapply _))
    }

    implicit def jsonReadsApidocOrganizationForm: play.api.libs.json.Reads[OrganizationForm] = {
      (
        (__ \ "name").read[String] and
        (__ \ "key").readNullable[String] and
        (__ \ "domains").readNullable[Seq[String]].map(_.getOrElse(Nil)) and
        (__ \ "metadata").readNullable[com.gilt.apidoc.models.OrganizationMetadataForm]
      )(OrganizationForm.apply _)
    }

    implicit def jsonWritesApidocOrganizationForm: play.api.libs.json.Writes[OrganizationForm] = {
      (
        (__ \ "name").write[String] and
        (__ \ "key").write[scala.Option[String]] and
        (__ \ "domains").write[Seq[String]] and
        (__ \ "metadata").write[scala.Option[com.gilt.apidoc.models.OrganizationMetadataForm]]
      )(unlift(OrganizationForm.unapply _))
    }

    implicit def jsonReadsApidocOrganizationMetadata: play.api.libs.json.Reads[OrganizationMetadata] = {
      (
        (__ \ "visibility").readNullable[com.gilt.apidoc.models.Visibility] and
        (__ \ "package_name").readNullable[String]
      )(OrganizationMetadata.apply _)
    }

    implicit def jsonWritesApidocOrganizationMetadata: play.api.libs.json.Writes[OrganizationMetadata] = {
      (
        (__ \ "visibility").write[scala.Option[com.gilt.apidoc.models.Visibility]] and
        (__ \ "package_name").write[scala.Option[String]]
      )(unlift(OrganizationMetadata.unapply _))
    }

    implicit def jsonReadsApidocOrganizationMetadataForm: play.api.libs.json.Reads[OrganizationMetadataForm] = {
      (
        (__ \ "visibility").readNullable[com.gilt.apidoc.models.Visibility] and
        (__ \ "package_name").readNullable[String]
      )(OrganizationMetadataForm.apply _)
    }

    implicit def jsonWritesApidocOrganizationMetadataForm: play.api.libs.json.Writes[OrganizationMetadataForm] = {
      (
        (__ \ "visibility").write[scala.Option[com.gilt.apidoc.models.Visibility]] and
        (__ \ "package_name").write[scala.Option[String]]
      )(unlift(OrganizationMetadataForm.unapply _))
    }

    implicit def jsonReadsApidocService: play.api.libs.json.Reads[Service] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "name").read[String] and
        (__ \ "key").read[String] and
        (__ \ "visibility").read[com.gilt.apidoc.models.Visibility] and
        (__ \ "description").readNullable[String]
      )(Service.apply _)
    }

    implicit def jsonWritesApidocService: play.api.libs.json.Writes[Service] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "name").write[String] and
        (__ \ "key").write[String] and
        (__ \ "visibility").write[com.gilt.apidoc.models.Visibility] and
        (__ \ "description").write[scala.Option[String]]
      )(unlift(Service.unapply _))
    }

    implicit def jsonReadsApidocSubscription: play.api.libs.json.Reads[Subscription] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "organization").read[com.gilt.apidoc.models.Organization] and
        (__ \ "user").read[com.gilt.apidoc.models.User] and
        (__ \ "publication").read[com.gilt.apidoc.models.Publication]
      )(Subscription.apply _)
    }

    implicit def jsonWritesApidocSubscription: play.api.libs.json.Writes[Subscription] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "organization").write[com.gilt.apidoc.models.Organization] and
        (__ \ "user").write[com.gilt.apidoc.models.User] and
        (__ \ "publication").write[com.gilt.apidoc.models.Publication]
      )(unlift(Subscription.unapply _))
    }

    implicit def jsonReadsApidocSubscriptionForm: play.api.libs.json.Reads[SubscriptionForm] = {
      (
        (__ \ "organization_key").read[String] and
        (__ \ "user_guid").read[_root_.java.util.UUID] and
        (__ \ "publication").read[com.gilt.apidoc.models.Publication]
      )(SubscriptionForm.apply _)
    }

    implicit def jsonWritesApidocSubscriptionForm: play.api.libs.json.Writes[SubscriptionForm] = {
      (
        (__ \ "organization_key").write[String] and
        (__ \ "user_guid").write[_root_.java.util.UUID] and
        (__ \ "publication").write[com.gilt.apidoc.models.Publication]
      )(unlift(SubscriptionForm.unapply _))
    }

    implicit def jsonReadsApidocToken: play.api.libs.json.Reads[Token] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "user").read[com.gilt.apidoc.models.User] and
        (__ \ "token").read[String] and
        (__ \ "description").readNullable[String]
      )(Token.apply _)
    }

    implicit def jsonWritesApidocToken: play.api.libs.json.Writes[Token] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "user").write[com.gilt.apidoc.models.User] and
        (__ \ "token").write[String] and
        (__ \ "description").write[scala.Option[String]]
      )(unlift(Token.unapply _))
    }

    implicit def jsonReadsApidocTokenForm: play.api.libs.json.Reads[TokenForm] = {
      (
        (__ \ "user_guid").read[_root_.java.util.UUID] and
        (__ \ "description").readNullable[String]
      )(TokenForm.apply _)
    }

    implicit def jsonWritesApidocTokenForm: play.api.libs.json.Writes[TokenForm] = {
      (
        (__ \ "user_guid").write[_root_.java.util.UUID] and
        (__ \ "description").write[scala.Option[String]]
      )(unlift(TokenForm.unapply _))
    }

    implicit def jsonReadsApidocUser: play.api.libs.json.Reads[User] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "email").read[String] and
        (__ \ "name").readNullable[String]
      )(User.apply _)
    }

    implicit def jsonWritesApidocUser: play.api.libs.json.Writes[User] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "email").write[String] and
        (__ \ "name").write[scala.Option[String]]
      )(unlift(User.unapply _))
    }

    implicit def jsonReadsApidocValidation: play.api.libs.json.Reads[Validation] = {
      (
        (__ \ "valid").read[Boolean] and
        (__ \ "errors").readNullable[Seq[String]].map(_.getOrElse(Nil))
      )(Validation.apply _)
    }

    implicit def jsonWritesApidocValidation: play.api.libs.json.Writes[Validation] = {
      (
        (__ \ "valid").write[Boolean] and
        (__ \ "errors").write[Seq[String]]
      )(unlift(Validation.unapply _))
    }

    implicit def jsonReadsApidocVersion: play.api.libs.json.Reads[Version] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "version").read[String] and
        (__ \ "json").read[String]
      )(Version.apply _)
    }

    implicit def jsonWritesApidocVersion: play.api.libs.json.Writes[Version] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "version").write[String] and
        (__ \ "json").write[String]
      )(unlift(Version.unapply _))
    }

    implicit def jsonReadsApidocWatch: play.api.libs.json.Reads[Watch] = {
      (
        (__ \ "guid").read[_root_.java.util.UUID] and
        (__ \ "user").read[com.gilt.apidoc.models.User] and
        (__ \ "organization").read[com.gilt.apidoc.models.Organization] and
        (__ \ "service").read[com.gilt.apidoc.models.Service]
      )(Watch.apply _)
    }

    implicit def jsonWritesApidocWatch: play.api.libs.json.Writes[Watch] = {
      (
        (__ \ "guid").write[_root_.java.util.UUID] and
        (__ \ "user").write[com.gilt.apidoc.models.User] and
        (__ \ "organization").write[com.gilt.apidoc.models.Organization] and
        (__ \ "service").write[com.gilt.apidoc.models.Service]
      )(unlift(Watch.unapply _))
    }

    implicit def jsonReadsApidocWatchForm: play.api.libs.json.Reads[WatchForm] = {
      (
        (__ \ "user_guid").read[_root_.java.util.UUID] and
        (__ \ "organization_key").read[String] and
        (__ \ "service_key").read[String]
      )(WatchForm.apply _)
    }

    implicit def jsonWritesApidocWatchForm: play.api.libs.json.Writes[WatchForm] = {
      (
        (__ \ "user_guid").write[_root_.java.util.UUID] and
        (__ \ "organization_key").write[String] and
        (__ \ "service_key").write[String]
      )(unlift(WatchForm.unapply _))
    }
  }
}