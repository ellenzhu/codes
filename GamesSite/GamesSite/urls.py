from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'GamesSite.views.home', name='home'),
    url(r'^accounts/', include("users_profiles.urls")),
    url(r'^$', include("GamesApp.urls")),
    url(r'^games/', include("GamesApp.urls")),
    url(r'^admin/', include(admin.site.urls)),  # user authentication urls ***Ala***
    url(r'^accounts/login/$', 'GamesSite.views.login'),
    url(r'^accounts/logged_in/$', 'GamesSite.views.logged_in'),
    url(r'^accounts/invalid/$', 'GamesSite.views.invalid'),
    url(r'^accounts/logout/$', 'GamesSite.views.logout'),
    url(r'^accounts/auth/$', 'GamesSite.views.user_authentication'),
    url(r'^accounts/register2/$', 'GamesSite.views.user_registration'),
    url(r'^accounts/successful_registration/$', 'GamesSite.views.successful_registration'),
    url('', include('social.apps.django_app.urls', namespace='social'))
   # url(r'^search/$', include('haystack.urls')),
    #url(r'^search/$', include('haystack.urls')),
    #url(r'^accounts/register1/$', 'GamesSite.views.register_user'),
     )
