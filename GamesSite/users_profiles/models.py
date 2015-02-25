from datetime import datetime
from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver
#from GamesApp.models import Game

import logging
logr = logging.getLogger(__name__)


class UserProfile(models.Model):
# instead of models.Model it's possible that we put Player so when creating a user profile we create player by default
    # This line is required. Links UserProfile to a User model instance.
    user = models.OneToOneField(User)
    # The additional attributes we wish to include.
    category = models.CharField(max_length=20, choices=(("Developer", "Developer"),("Gamer", "Gamer")))
    receive_news = models.BooleanField(default=True)
    picture = models.ImageField(upload_to='profile_images', blank=True)
    name = models.CharField(max_length=10)#remove this
    created_on = models.DateTimeField(auto_now_add=True, default=datetime.now)
    updated_on = models.DateTimeField(auto_now=True, default=datetime.now)
    # Override the __unicode__() method to return out something meaningful!

    def __unicode__(self):

        return self.user.username

User.profile = property(lambda u: UserProfile.objects.get_or_create(user=u)[0])
#  whenever UserProfile initiated execute the property