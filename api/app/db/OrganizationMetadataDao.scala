package db

import com.gilt.apidoc.models.{Organization, OrganizationMetadata, OrganizationMetadataForm, User, Version, Visibility}
import com.gilt.apidoc.models.json._
import lib.{Text, Validation, ValidationError}
import anorm._
import play.api.db._
import play.api.Play.current
import play.api.libs.json._
import java.util.UUID

object OrganizationMetadataDao {

  private val BaseQuery = """
    select guid::varchar, organization_guid::varchar, visibility, package_name
      from organization_metadata
     where deleted_at is null
       and organization_guid = {organization_guid}::uuid
  """

  private val InsertQuery = """
    insert into organization_metadata
    (guid, organization_guid, visibility, package_name, created_by_guid)
    values
    ({guid}::uuid, {organization_guid}::uuid, {visibility}, {package_name}, {created_by_guid}::uuid)
  """

  private val SoftDeleteQuery = """
    update organization_metadata
       set deleted_by_guid = {deleted_by_guid}::uuid,
           deleted_at = now()
     where organization_guid = {organization_guid}::uuid
       and deleted_at is null
  """

  def validate(form: OrganizationMetadataForm): Seq[ValidationError] = {
    val visibilityErrors = form.visibility match {
      case None => Seq.empty
      case Some(vis) => {
        Visibility(vis.toString) match {
          case Visibility.UNDEFINED(name) => Seq(s"Invalid visibility[$vis]")
          case _ => Seq.empty
        }
      }
    }

    val packageNameErrors = form.packageName match {
      case None => Seq.empty
      case Some(name: String) => {
        if (isValidPackageName(name)) {
          Seq.empty
        } else {
          Seq("Package name is not valid. Must be a dot separated list of valid names (start wtih letter, contains only a-z, A-Z, 0-9 and _ characters")
        }
      }
    }

    Validation.errors(visibilityErrors ++ packageNameErrors)
  }

  /**
   * A valid package name consists of a dot separated list of strings,
   * each of which is a valid name.
   */
  private[db] def isValidPackageName(name: String): Boolean = {
    name.split("\\.").find(!Text.isValidName(_)) match {
      case None => true
      case _ => false
    }
  }

  def upsert(user: User, org: Organization, form: OrganizationMetadataForm): OrganizationMetadata = {
    DB.withTransaction { implicit c =>
      softDelete(c, user, org)
      create(c, user, org, form)
    }
  }

  private[db] def create(createdBy: User, org: Organization, form: OrganizationMetadataForm): OrganizationMetadata = {
    DB.withConnection { implicit c =>
      create(c, createdBy, org, form)
    }
  }

  private[db] def create(implicit c: java.sql.Connection, createdBy: User, org: Organization, form: OrganizationMetadataForm): OrganizationMetadata = {
    val metadata = OrganizationMetadata(
      visibility = form.visibility,
      packageName = form.packageName
    )
    val guid = UUID.randomUUID.toString

    SQL(InsertQuery).on(
      'guid -> guid,
      'organization_guid -> org.guid,
      'visibility -> metadata.visibility.map(_.toString),
      'package_name -> metadata.packageName.map(_.trim),
      'created_by_guid -> createdBy.guid
    ).execute()

    metadata
  }

  private[db] def softDelete(deletedBy: User, org: Organization) {
    DB.withConnection { implicit c =>
      softDelete(c, deletedBy, org)
    }
  }

  private[db] def softDelete(implicit c: java.sql.Connection, deletedBy: User, org: Organization) {
    SQL(SoftDeleteQuery).on('deleted_by_guid -> deletedBy.guid, 'organization_guid -> org.guid).execute()
  }

  def findByOrganizationGuid(organizationGuid: UUID): Option[OrganizationMetadata] = {
    DB.withConnection { implicit c =>
      SQL(BaseQuery).on('organization_guid -> organizationGuid.toString)().toList.map { row =>
        OrganizationMetadata(
          visibility = row[Option[String]]("visibility").map(Visibility(_)),
          packageName = row[Option[String]]("package_name")
        )
      }.toSeq.headOption
    }
  }

}
