__author__ = 'alabrazi'
from tastypie.resources import ModelResource
from tastypie.constants import ALL
from GamesApp.models import Game


class GamesResource(ModelResource):

    class Meta:

        queryset = Game.objects.all()
        resource_name = 'game' # api/game **Ala**
        filtering = {'description': 'contains' } # description__contains
        #  http://127.0.0.1:8000/games/api/game/?format=json&description__contains=finish