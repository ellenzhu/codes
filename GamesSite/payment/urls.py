from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'GamesSite.views.home', name='home'),
    url(r'^(?P<game_id>\d+)/$', 'payment.views.buy_game'),
    url(r'^payment/success/$', 'payment.views.buy_success'),
    url(r'^payment/cancel/$', 'payment.views.buy_cancel_or_fail'),
    url(r'^payment/fail/$', 'payment.views.buy_cancel_or_fail'),
   # url(r'^search/$', include('haystack.urls')),
    #url(r'^search/$', include('haystack.urls')),
    #url(r'^accounts/register1/$', 'GamesSite.views.register_user'),
     )
