from django import forms
from users_profiles.models import UserProfile
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User

class UserProfileForm(forms.ModelForm):

    class Meta:
        model = UserProfile
        fields = ('receive_news',  'picture')
        #todo how to change user category from player to developer and viceverca
        #todo how to determine the category of the user created in 3d party login process

class ShortUserProfileForm(forms.ModelForm):

    class Meta:
        model = UserProfile
        fields = ('category',)

class UserProfileForm2(forms.ModelForm):
    #password = forms.CharField(widget=forms.PasswordInput())
    #category = forms.ChoiceField(choices=(("Developer", "Developer"),("Gamer", "Gamer")))

    class Meta:
        model = User
        fields = ( 'first_name', 'last_name', 'email',)