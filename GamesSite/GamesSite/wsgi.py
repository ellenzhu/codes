"""
WSGI config for GamesSite project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/1.7/howto/deployment/wsgi/
"""

import os
import sys

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "GamesSite.settings")

from django.core.wsgi import get_wsgi_application
application = get_wsgi_application()

try:
    from dj_static import Cling
    application = Cling(get_wsgi_application())

except:
	pass
