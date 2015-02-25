__author__ = 'alabrazi'
from django.apps import AppConfig
import watson

class MyAppConfig(AppConfig):
    name = "GamesApp"
    def ready(self):
        Game = self.get_model("Game")
        watson.register(Game)
