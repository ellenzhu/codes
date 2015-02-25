__author__ = 'alabrazi'
from django import forms
from GamesApp.models import Game, Players, Score

class GameForm(forms.ModelForm):

    class Meta:
        model = Game
        fields=( 'game_name','description', 'price', 'code', 'image' )
        
        
class PaymentForm(forms.Form):
    
    success_url = forms.URLField()
    