"""
Django settings for GamesSite project.

For more information on this file, see
https://docs.djangoproject.com/en/1.7/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.7/ref/settings/
"""

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os
BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
PROJECT_DIRECTORY = os.getcwd()  #  Users/alabrazi/Documents/OneDrive/wsd-project/GamesSite or os.path.dirname(__file__)
SITE_ID = 1
# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.7/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = '$v9l#cm$0p^lr^muw0s7k!@owg0$w2+&dm_c6+wqk=$0$3=-zm'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = False

TEMPLATE_DEBUG = True

ALLOWED_HOSTS = ['localhost', 'sheltered-earth-9730.herokuapp.com']

WHOOSH_INDEX = os.path.join(PROJECT_DIRECTORY,'whoosh/') #  Path to store search index  **Ala**

HAYSTACK_CONNECTIONS = {
    'default': {
        'ENGINE': 'haystack.backends.whoosh_backend.WhooshEngine',
        'PATH': WHOOSH_INDEX,
    },
}
# connect haystack to whoosh don't know what is going on here **Ala**
#
# Application definition


TEMPLATE_CONTEXT_PROCESSORS = ('social.apps.django_app.context_processors.backends',
                               'social.apps.django_app.context_processors.login_redirect',
                               'django.contrib.auth.context_processors.auth',
                               'django.core.context_processors.debug',
   'django.core.context_processors.i18n',
   'django.core.context_processors.media',
   'django.core.context_processors.static',
   'django.core.context_processors.tz',
   'django.contrib.messages.context_processors.messages',

)
INSTALLED_APPS = (
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'GamesApp',  # Added *Ala*
    'users_profiles',
    'whoosh',
    'haystack',
    'bootstrap3',
    'watson',
    'social.apps.django_app.default',

)



MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

ROOT_URLCONF = 'GamesSite.urls'

WSGI_APPLICATION = 'GamesSite.wsgi.application'


# Database
# https://docs.djangoproject.com/en/1.7/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
    }
}

# Internationalization
# https://docs.djangoproject.com/en/1.7/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True

#DEFAULT_FROM_EMAIL = 'webmaster@example.com'
#TEMPLATE_DIRS = ('/home/templates/mike', '/home/templates/john')
# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.7/howto/static-files/
#STATIC_ROOT = os.path.join(os.getcwd(),'static/')
MEDIA_ROOT = 'static'
#  This is where the default path for files go when uploading them by default it's the site/poject root folder
STATIC_URL = '/static/'

STATICFILES_DIRS = (
    os.path.join(os.getcwd(),'static/'),

)
# STATICFILES_DIRS determine the location for the static file when using {%static "/js/ajax.js"%} inside template
#  os.path.join(BASE_DIR, "static") will be created in /var/www/static/ and it will hold all the static files
# This should be set to a list or tuple of strings that contain full paths to your additional files directory(ies) e.g.:
# STATICFILES_DIRS = (
#     "/home/special.polls.com/polls/static",
#     "/home/polls.com/polls/static",
#     "/opt/webfiles/common",
# )
# STATICFILES_DIRS = (
#     # ...
#     ("downloads", "/opt/webfiles/stats"),
# )
MEDIA_URL = ""
TEMPLATE_DIRS = (
    os.path.join(BASE_DIR,  'templates'),
)
AUTHENTICATION_BACKENDS = (
   'social.backends.facebook.FacebookOAuth2',
   'social.backends.google.GoogleOAuth2',
   'social.backends.twitter.TwitterOAuth',
   'django.contrib.auth.backends.ModelBackend',
)

SOCIAL_AUTH_FACEBOOK_KEY = '1534802396772090'
SOCIAL_AUTH_FACEBOOK_SECRET = '212e28cbc11a5c4c9fd36194a54660c0'
LOGIN_REDIRECT_URL = '/'
SOCIAL_AUTH_GOOGLE_OAUTH2_KEY = '385650828102-a256g9ae0f0egbb42bv4fmcjr6hhv3b0.apps.googleusercontent.com'
SOCIAL_AUTH_GOOGLE_OAUTH2_SECRET = 'uNSzTyiT6UG0inSkdc9LBaYq'

SOCIAL_AUTH_TWITTER_KEY = 'kmWOw5WY9Fz3rF8ZqKCRuxFLl'
SOCIAL_AUTH_TWITTER_SECRET = 'CGYbAwgCzkXGR3O6QPV7BlSJtejJAZJWMUYBiCoA4GM2ZTkH1s'