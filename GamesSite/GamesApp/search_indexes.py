__author__ = 'alabrazi'
import datetime
from haystack import indexes
from GamesApp.models import Game


class GameIndex(indexes.SearchIndex, indexes.Indexable):

    text = indexes.CharField(document=True, use_template=True)
    # template exist inside site template folder. The name of folder is similar to the app game and the name
    # of the file is app_text  ... **Ala**
    content_auto_complete_description = indexes.EdgeNgramField(model_attr='description')

    def get_model(self):

        return Game

    def index_queryset(self, using=None):

        return self.get_model().objects.all()