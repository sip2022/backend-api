import logging as log
import os
import tarfile
from datetime import datetime

from gcloud import storage
from oauth2client.service_account import ServiceAccountCredentials

# BUCKET_NAME = os.environ['bucket_name']
# # dict to hold the credentials of GCP auth
# CREDENTIALS = {
#     'type': 'service_account',
#     'client_id': os.environ['client_id'],
#     'client_email': os.environ['client_email'],
#     'private_key_id': os.environ['private_key_id'],
#     'private_key': os.environ['private_key'],
# }
BUCKET_NAME = 'sip-db-bucket'
CREDENTIALS = {
    'type': 'service_account',
    'client_id': '115360419542537508247',
    'client_email': '310985208451-compute@developer.gserviceaccount.com',
    'private_key_id': '2439b6422efdd843b17f9f7374b501fa573f2ebe',
    'private_key': '-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCiJQD2AWFnFAtO\nzyAJttJtkuN3o/nYL2obdZSLjGH8CfLs0IByK3ORHC1cdmmKiIp0FZK59hGItLJ4\n+PWwPm1BV/SVGMnEyFuEa9mVCZYjDm09NwI/CZq8lCC6pSNcsT2yibXym5Q1Rs3H\nTIB0zEi1wDWTwxX8qW+rL28WPH/fnoCcPqfaYeME4EYhdBbIwrQn3kcdrQhqdNzP\nro4F3IXqaQ6wVhu1Pzo94B8awQa2jg8E+BGm5313YsCZ7x/2qyS/iYOoJ8wlpsex\nxlO4mVjdZwt8KqBLRl48YcTX2Z7AzklmFLFdcALqoYMyI5XmqCOHNA08Fi51XD4R\nkGsoe04lAgMBAAECggEAKTdExkPVkZEryq2l1DJPzSCgh6H7+XMcArzhtJNLuPiC\ncqYRcHQRHVrPQQisjVVl9FojgPfGvzxHfa6zLUjBFvIXruqtJ0NS2BdeKJ9WNbUO\nGZhpDsMPPU5d/3PIyGAZcc/Fm1Tm2KAV4MpWgNoIxYKFW/kkFqSvSRF2qG1ah+nn\nmppWHSlIPVErR+QpIfn1+U6Tm+w9ZiEPhN3DP+GwYAo4sP8i6U2L8NsFmNNGDBnh\nh69aPzqb720kYhEMZ6IICGcoqfwqcualqDQdvaauP+TS7K+0UOkqjzSJL7ri6wOp\nHWxn49NgTRPsRvtOZnBwlf+5Nl13vFYHY0DhTsYZ5QKBgQDcdsuaQD20KTT80Il4\nmYQny2grhiw9EyyXbzbFgTeTVds3Jff3/HKUMYaLqDU2Ppw7LMY3bJpNI4uuNfcR\nxTHZs+UvP56p7supAYiKQZ1y5/LFLwLhSMAQymHsfocmFWO2SytSaGk96i3EJS5S\nZHi6g4PPGi8PRGXtemqGzTJz7wKBgQC8R7gKTlBrLhOO4cJ6j8NxhrJswMQY3LUf\nEV6IjO2ZYO1Ke5pFtQMXJMi8AOSvHcGkXe+m9EqW1zZGFQWOTExJw2nrIFgDimPN\nJy+aoSj8ncyfaDzcRlEUQjyxU+1ciLVlGEadDUl17mRzWplREPKeIbIRcZIUNPIj\noy0i+5J7KwKBgQDX0l/dIHiHTzO1Lvz1F+wc/O58dTwHiibEFLacwLPEQ2933G6U\n0Qjl9FnPjBqTgNV2xI1DIsknLN4H7IJzXgVAJvD8wR41IzUyvPKKzlMxcsnhqPT7\n0Dld9pHIb9EkQho8EfhWEyq71u14sCFZvUvG4wp9Cxf4rS/PPGDemDCE9wKBgCVI\nCkA4K6JdeE/1kh3Us72aD2CLdwj5QwAI33Aof6+8SqBSA0VER31gskLP5nhqavqS\nAy9K9i4SJVQRO41mvEBq6mE9gVAKKiLoXHvA563FzUvPRyXS4qEhUIVIehq1kz2W\nZf0ZMW+hqdBRo0QzF+eKAomDtIo9xaia2ggwSlwLAoGAUQJeChLuHx5jsvWEDc4G\nSvi2/mNJMTEZW1W/RMyfMw6j4YagNVmLh/2cYbgEggy9F9zBDcSDwuaA6PWNJAQl\niuR6E+CzGuM7gRajyWySglbtwNQh91YHE2EajIiO7Irs34YctH2826VM6r8NkGLz\nKgZv0Cse7S1NVKRb5TVtD94=\n-----END PRIVATE KEY-----\n',
}


def generate_dump() -> str:
    pass


def make_tarfile_from_dump(dump_filename) -> str:
    """
    Gets the dump filename and creates a tar file with the same name, then deletes the dump file.
    :param dump_filename: name of the sql dump file
    :return: name of the tar file created
    """
    tar_filenme = "backup/" + dump_filename
    try:
        with tarfile.open(os, "w:gz") as tar:
            tar.add("backup/" + dump_filename)
        tar.close()
        log.info(
            f'File {dump_filename} successfully compressed to {dump_filename} with timestamp {datetime.now()}.')
        os.remove(dump_filename)
        log.info(f'File {dump_filename} successfully deleted.')
        return tar_filenme
    except Exception:
        log.error(f'Error while trying to compress {dump_filename}')


def upload_tarfile(filename: str, credentials: str) -> None:
    client = storage.Client(credentials=credentials)
    filename = filename
    try:
        bucket = client.get_bucket(BUCKET_NAME)
        blob = bucket.blob('myfile')
        blob.upload_from_filename(filename)
        log.info(f'File {filename} successfully uploaded to {BUCKET_NAME} with timestamp {datetime.now()}.')
    except Exception:
        log.error(f'Error while trying to update {filename} to {BUCKET_NAME} with timestamp {datetime.now()}')
    return


dump_name: str = ''
tar_name: str = ''

log.basicConfig(level=log.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

log.info('hi from python backuper!')
# dump_name: str = generate_dump()
# tar_name: str = make_tarfile_from_dump(dump_name)
upload_tarfile(tar_name or 'myfile.txt', ServiceAccountCredentials.from_json_keyfile_dict(CREDENTIALS))
