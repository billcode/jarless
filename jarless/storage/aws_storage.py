import boto3
from boto3.s3.transfer import TransferConfig

from jarless.ext.configuration import get_config_from_env

config = get_config_from_env()
_4HOURS = 14400


def upload_fileobj(fileobj, target_uri, **kwargs):
    bucket, key = _parse_s3_uri(target_uri)
    s3 = boto3.resource("s3", endpoint_url=config.STORAGE_DSN)
    s3_config = TransferConfig(
        multipart_threshold=1024 * 25, max_concurrency=10, multipart_chunksize=1024 * 25, use_threads=True
    )

    s3.meta.client.upload_fileobj(
        fileobj,
        bucket,
        key,
        Config=s3_config,
    )
    return target_uri


def get_public_download_file_url(remote_uri):
    if not remote_uri:
        return None

    bucket, key = _parse_s3_uri(remote_uri)
    s3 = boto3.client("s3", endpoint_url=config.STORAGE_DSN)

    return s3.generate_presigned_url(
        ClientMethod="get_object",
        Params={"Bucket": bucket, "Key": key},
        ExpiresIn=_4HOURS,
    )


def _parse_s3_uri(uri):
    if not uri.startswith("s3://"):
        raise RuntimeError('Invalid S3 URI: "{}". URI must starts with "s3://"'.format(uri))

    _, bucket_and_path = uri.split("://", 1)
    bucket, key = bucket_and_path.split("/", 1)
    return bucket, key
