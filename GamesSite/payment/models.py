from django.db import models
from GamesApp.models import Game
from django.contrib.auth.models import User
from django.db import models
from datetime import datetime
from django.conf import settings
from GamesSite import settings
import os
import os.path
from time import time
from users_profiles.models import UserProfile
# Create your models here.
class Payment(models.Model):

    pid = models.CharField(max_length=200, primary_key=True)
    user = models.ForeignKey(User, related_name = 'userpayments')
    created_on = models.DateTimeField(auto_now_add=True, default=datetime.now, blank=True)
    updated_on = models.DateTimeField(auto_now=True, default=datetime.now, blank=True)
    amount = models.DecimalField(max_digits=10, decimal_places=2)
    game = models.ForeignKey(Game, related_name = 'gamepayments')
    STATUS_CHOICES = (
                      ('S', 'Success'),
                      ('C', 'Cancel'),
                      ('E', 'Error'),
                      ('P', 'Pending'),
                    )
    status = models.CharField(max_length=1, choices=STATUS_CHOICES)

    def __str__(self):
        return self.pid +": "+ self.user.username +" - "+ self.game.game_name +" -- "+ self.status