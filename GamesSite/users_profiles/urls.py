from django.conf.urls import patterns, include, url

urlpatterns = patterns('',
        url(r'^profile/$', 'users_profiles.views.user_profile'),
)