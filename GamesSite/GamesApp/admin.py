from django.contrib import admin
#from users_profiles.models import UserProfile
from GamesApp.models import Game, Players, Developer, Score, GameSave
from payment.models import Payment

#admin.site.register(UserProfile)
admin.site.register(Game)
admin.site.register(Players)
admin.site.register(Developer)
admin.site.register(Score)
admin.site.register(Payment)
admin.site.register(GameSave)

# Register your models here.
