"""Add task secret

Revision ID: 68bfe8ec8c50
Revises: 3e9c653ec0d3
Create Date: 2020-11-29 14:28:33.968453

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = "68bfe8ec8c50"
down_revision = "3e9c653ec0d3"
branch_labels = None
depends_on = None


def upgrade():
    op.add_column("tasks", sa.Column("secrets", sa.String(length=128), nullable=True))


def downgrade():
    op.drop_column("tasks", "secrets")
