from django.contrib.auth.models import User
from django.db import models
from datetime import datetime
from django.conf import settings
from GamesSite import settings
import os
import os.path
from time import time
from users_profiles.models import UserProfile

def get_upload_file_name(instance, filename):
    return "uploaded_files/%s_%s" % (str(time()).replace('.','_'), filename)

class Game(models.Model):

    created_on = models.DateTimeField(auto_now_add=True, default=datetime.now, blank=True)
    updated_on = models.DateTimeField(auto_now=True, default=datetime.now, blank=True)
    game_name = models.CharField(max_length=200, unique=True)
    description=models.TextField()
    image=models.ImageField(upload_to = get_upload_file_name, blank=True, null=True)
    #Todo if searh fail add category = models.CharField(max_length=20, choices=(("Action", "Action"),("Adventure", "Adventure")))
    price = models.DecimalField(max_digits=10, decimal_places=2)
    code = models.FileField(upload_to = get_upload_file_name, blank=True, null=True)
    #Todo call the function to rename the file before uploading it
    #Todo function to modify the images before storing them

    def __unicode__(self):

        return self.game_name

    def __str__(self):
        return self.game_name


class Players(models.Model):

    user = models.OneToOneField(User)
    game = models.ManyToManyField(Game, related_name='players')


    def __str__(self):
        return self.user.username

class Developer(Players):

    developed_games = models.ManyToManyField(Game, related_name='developers')

    def __str__(self):

        return self.user.username


class Score(models.Model):

    game = models.ForeignKey(Game, related_name = 'gamescores')
    name = models.ForeignKey(Players, related_name = 'playerscores')
    score = models.DecimalField(max_digits=15, decimal_places=2)

    def __str__(self):
        return self.name.user.username +" - "+ self.game.game_name +" - "+ str(self.score)
    
class GameSave(models.Model):
    
    game = models.ForeignKey(Game, related_name = 'gamesaves')
    player = models.ForeignKey(Players, related_name = 'playersaves')
    gameState = models.TextField()
    
    def __str__(self):
        return self.player.user.username +" - "+ self.game.game_name

# class Payment(models.Model):
#
#     pid = models.CharField(max_length=200, primary_key=True)
#     user = models.ForeignKey(User, related_name = 'userpayments')
#     created_on = models.DateTimeField(auto_now_add=True, default=datetime.now, blank=True)
#     updated_on = models.DateTimeField(auto_now=True, default=datetime.now, blank=True)
#     amount = models.DecimalField(max_digits=10, decimal_places=2)
#     game = models.ForeignKey(Game, related_name = 'gamepayments')
#     STATUS_CHOICES = (
#                       ('S', 'Success'),
#                       ('C', 'Cancel'),
#                       ('E', 'Error'),
#                       ('P', 'Pending'),
#                     )
#     status = models.CharField(max_length=1, choices=STATUS_CHOICES)
#
#     def __str__(self):
#         return self.pid +": "+ self.user.username +" - "+ self.game.game_name
    
# import watson
# watson.register(Game.objects.all(), fields=("game_name", "description"))