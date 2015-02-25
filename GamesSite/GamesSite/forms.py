__author__ = 'alabrazi'
from django.contrib.auth.forms import UserCreationForm
from django import forms
from django.contrib.auth.models import User
from users_profiles.models import UserProfile

class MyRegistrationForm(UserCreationForm):
# didn't use this as category is not getting saved for a reason which I don't know
#email does work but not the category, email is part of User class whereas category is not
    email = forms.EmailField(required=True)
    #category = forms.ChoiceField(choices=(("Developer", "Developer"),("Gamer", "Gamer")))

    class Meta:

        model = User
        fields = ('username', 'email', 'password1', 'password2')

    def save(self, commit=True):

        user = super(MyRegistrationForm, self).save(commit=False)
        user.email = self.cleaned_data['email']
        #user.category=self.cleaned_data['category']
        # user.set_password(self.cleaned_data['password1'])
        if commit:
            user.save()
        return user

class UserForm(UserCreationForm): # this one is used for registeration plus other one defined in user_profiles
    #password = forms.CharField(widget=forms.PasswordInput())

    class Meta:
        model = User
        fields = ('username', 'email', 'password1','password2')






