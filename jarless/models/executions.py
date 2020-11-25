from datetime import datetime, timezone
import sqlalchemy as sa
from sqlalchemy.dialects.postgresql import JSONB

from jarless.ext.database import db


class Package(db.Model):

    __tablename__ = "packages"
    __table_args__ = (
        sa.UniqueConstraint(
            "name",
            name="ix_unique_name_constraint",
        ),
    )

    id = sa.Column(sa.Integer, primary_key=True, autoincrement=True)
    name = sa.Column(sa.String(128), nullable=False, unique=True)
    definition = sa.Column(JSONB, nullable=False, default={})
    created_at = sa.Column(sa.DateTime(timezone=True), default=lambda: datetime.now(timezone.utc))


class Execution(db.Model):

    __tablename__ = "executions"

    STATUS_QUEUED = "PENDING"
    STATUS_STARTED = "STARTED"
    STATUS_SUCCESS = "SUCCESS"
    STATUS_FAILURE = "FAILURE"

    id = sa.Column(sa.Integer, primary_key=True, autoincrement=True)
    status = sa.Column(sa.String(32), server_default="QUEUED", nullable=False)
    description = sa.Column(sa.String(256), server_default=None, nullable=True)
    created_at = sa.Column(sa.DateTime(timezone=True), default=lambda: datetime.now(timezone.utc))
    started_at = sa.Column(sa.DateTime(timezone=True), nullable=True)
    finished_at = sa.Column(sa.DateTime(timezone=True), nullable=True)


class Task(db.Model):

    __tablename__ = "tasks"

    id = sa.Column(sa.Integer, primary_key=True)
    execution_id = sa.Column(
        sa.Integer,
        sa.ForeignKey("executions.id", onupdate="CASCADE", ondelete="CASCADE"),
        nullable=False,
        index=True,
    )
    package_id = sa.Column(
        sa.Integer,
        sa.ForeignKey("packages.id", onupdate="CASCADE", ondelete="CASCADE"),
        nullable=False,
        index=True,
    )
    inputs = sa.Column(JSONB, nullable=True, default={})
    outputs = sa.Column(JSONB(none_as_null=True), nullable=True)
    error = sa.Column(sa.Text, nullable=True)

    created_at = sa.Column(sa.DateTime(timezone=True), default=lambda: datetime.now(timezone.utc))
    started_at = sa.Column(sa.DateTime(timezone=True), nullable=True)
    finished_at = sa.Column(sa.DateTime(timezone=True), nullable=True)


class Log(db.Model):

    __tablename__ = "logs"
    __table_args__ = (sa.Index("ix_task_id_logs", "task_id"),)

    id = sa.Column(sa.Integer, primary_key=True, nullable=False)
    task_id = sa.Column(
        sa.Integer,
        sa.ForeignKey("tasks.id", onupdate="CASCADE", ondelete="CASCADE"),
        nullable=False,
        index=True,
    )
    level = sa.Column(sa.String(32), nullable=False)
    message = sa.Column(sa.String(256), nullable=False)
    created_at = sa.Column(sa.DateTime(timezone=True), nullable=False)
