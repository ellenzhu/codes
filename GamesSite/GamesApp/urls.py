__author__ = 'alabrazi'
from django.conf.urls import patterns, include, url
from django.contrib import admin
from GamesApp.api import GamesResource

games_resource = GamesResource()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'GamesSite.views.home', name='home'),
    url(r'^buy/', include("payment.urls")),
    url(r'^all/$', "GamesApp.views.get_all_games"),
    url(r'^$', "GamesApp.views.get_all_games"),
    url(r'^get/(?P<game_id>\d+)/$', 'GamesApp.views.get_game_by_id'),
    url(r'^create_game/$', 'GamesApp.views.create_game'),
    url(r'^delete_game/(?P<game_id>\d+)/$', 'GamesApp.views.delete_game'),
    url(r'^play/(?P<game_id>\d+)/$', 'GamesApp.views.play_game'),
    url(r'^score/(?P<game_id>\d+)/$', 'GamesApp.views.show_highscore'),
    url(r'^api/', include(games_resource.urls)),
    url(r'^mygames/$', 'GamesApp.views.my_games'),
    url(r'^mydevelopedgames/$', 'GamesApp.views.my_developed_games'),
    url(r'^edit/(?P<game_id>\d+)/$', 'GamesApp.views.edit_game'),
    #url(r'^search/', include('haystack.urls')),
    #url(r'^search/', 'GamesApp.views.search_watson'),
    url(r'^search/', 'GamesApp.views.search'),
    url(r'^salehistory/(?P<game_id>\d+)/$', 'GamesApp.views.show_salehistory'),


     #  http://127.0.0.1:8000/games/api/game/?format=json or http://127.0.0.1:8000/games/api/game/1/?format=json
    #  http://127.0.0.1:8000/games/api/game/?format=json&description__contains=finish
    )
